package com.gdu.prj09.service;

import java.io.PrintWriter;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gdu.prj09.dao.MemberDao;
import com.gdu.prj09.dto.AddressDto;
import com.gdu.prj09.dto.MemberDto;
import com.gdu.prj09.utils.MyPageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberDao memberDao;
  private final MyPageUtils myPageUtils;
  // root-context 에서 con-arg에 이거 넣어줄 때 선언한 순서대로 넣어줘야 한다.  
  
  @Override
  public ResponseEntity<Map<String, Object>> getMembers(int page, int display) {

    int total = memberDao.getTotalMemberCount();
    
    myPageUtils.setPaging(total, display, page);
    
    Map<String, Object> params = Map.of("begin", myPageUtils.getBegin() 
                                        , "end", myPageUtils.getEnd());
    
    List<AddressDto> members = memberDao.getMemberList(params);
    
    return new ResponseEntity<Map<String,Object>>(Map.of("members", members
                                                         , "total", total
                                                         , "paging", myPageUtils.getAsyncPaging()) 
                                                       , HttpStatus.OK);
    
  }

  @Override
  public ResponseEntity<Map<String, Object>> getMemberByNo(int memberNo) {

    //address 목록 가져오기 위한 작업
    int total = memberDao.getTotalAddressCountByNo(memberNo);
    int page = 1;
    int display = 20;
    
    myPageUtils.setPaging(total, display, page);
    
    Map<String, Object> params = Map.of("memberNo", memberNo
                                       , "begin", myPageUtils.getBegin()
                                       , "end", myPageUtils.getEnd());
    
    // 주소 목록을 가져옴
    List<AddressDto> addressList = memberDao.getAddressListByNo(params);
    
    // 회원 정보
    MemberDto member = memberDao.getMemberByNo(memberNo);
    
    return new ResponseEntity<Map<String,Object>>(Map.of("addressList", addressList
                                                         , "member", member)
                                               , HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Map<String, Object>> registerMember(Map<String, Object> map, HttpServletResponse response) {
    
    
    ResponseEntity<Map<String, Object>> result = null;
    
    try {
      
      MemberDto member = MemberDto.builder()
          .email((String)map.get("email"))  // 이게 맵에서 데이터 빼서 쓰는 방법!! object 타입이니까 이렇게 변환
          .name((String)map.get("name"))
          .gender((String)map.get("gender"))
          .build();
      
      int insertCount = memberDao.insertMember(member);           //insertCount 에서 1이나 0을 받아내는게 member.jsp 의 resData.
      
      AddressDto address = AddressDto.builder()
                             .zonecode((String)map.get("zonecode"))
                             .address((String)map.get("address"))
                             .detailAddress((String)map.get("detailAddress"))
                             .extraAddress((String)map.get("extraAddress"))
                             .member(member)
                          .build();
          
      insertCount += memberDao.insertAddress(address);
      
      
      result = new ResponseEntity<Map<String,Object>>(
          Map.of("insertCount", insertCount),
          HttpStatus.OK);  
      
    } catch (DuplicateKeyException e) {   // catch(Exception e) { 이름확인 : e.getClass().getName() } 를 통해서 무슨 예외가 던져지는지 확인할 수 있다.
      
      try {
        
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("이미 가입된 이메일입니다.");  //jqXHR 객체의 responseText 속성으로 확인 가능
        out.flush();
        out.close();
        
      } catch (Exception e2) {
        e.printStackTrace();
      }
    }
    
    return result;
    
    }

  @Override
  public ResponseEntity<Map<String, Object>> modifyMember(Map<String, Object> map) {

    int updateMemberCount = memberDao.updateMember(map);
    int updateAddressCount = memberDao.updateAddress(map);
    
  
    
    if(updateAddressCount == 0 ) {
      
      
      
      // address 테이블에 있는게 없어서 ㅋㅋㅋ (insert 된게 없으니까) 수정이 안되는걸 update -> insert 로 바꿔서 진행한 부분. 
      //updateCount = 0 이면 update -> insert로 바꾸자
      // 그래서 insert를 update 인듯이 슬쩍 중간에 넣어준 과정임
      AddressDto address = AddressDto.builder()
              .zonecode((String)map.get("zonecode"))
              .address((String)map.get("address"))
              .detailAddress((String)map.get("detailAddress"))
              .extraAddress((String)map.get("extraAddress"))
              .member(MemberDto.builder()
                             .memberNo(Integer.parseInt((String)map.get("memberNo")))
                           .build())
           .build();
          
         
          updateAddressCount = memberDao.insertAddress(address);                 
    }
      
    return new ResponseEntity<Map<String,Object>>(Map.of("updateCount", updateMemberCount + updateAddressCount), HttpStatus.OK); 
    // 이 map이 화면으로 넘어갈때 jackson이 map을 json 으로 바꿔준다. 성공시 저 json이 member.jsp로 전달되기를 기대
  }

  @Override
  public ResponseEntity<Map<String, Object>> removeMember(int memberNo) {
    return new ResponseEntity<Map<String,Object>>(Map.of("deleteCount", memberDao.deleteMember(memberNo)), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Map<String, Object>> removeMembers(String memberNoList) {
     // split을 사용해 넘어온 1,2,3,4... 이 String을 , 기준으로 배열로 만들어줌
    return new ResponseEntity<Map<String,Object>>(Map.of("deleteCount", memberDao.deleteMembers(Arrays.asList(memberNoList.split(",")))), HttpStatus.OK);
  }

}
