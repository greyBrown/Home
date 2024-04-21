package com.gdu.myapp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.myapp.dto.BbsDto;
import com.gdu.myapp.dto.UserDto;
import com.gdu.myapp.mapper.BbsMapper;
import com.gdu.myapp.utils.MyPageUtils;
import com.gdu.myapp.utils.MySecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BbsServiceImpl implements BbsService{
  
  private final BbsMapper bbsMapper;
  private final MyPageUtils myPageUtils;
  
  

  @Override
  public int registerBbs(HttpServletRequest request) {

    // 사용자가 입력한 contents
    String contents = MySecurityUtils.getPreventXss(request.getParameter("contents"));
    
    // 뷰에서 전달된 userNo
    int userNo = Integer.parseInt(request.getParameter("userNo")); // 로그인이 안되어 있으면 여기서 오류가 난다. 빈문자열이 넘어오기 때문 -> 인터셉터로 처리한다.

    // UserDto 객체 생성 (userNo 저장)
    UserDto user = new UserDto();
    user.setUserNo(userNo);
    
    // DB 에 저장할 BbsDto 객체   
    BbsDto bbs = BbsDto.builder()
                    .contents(contents)
                    .user(user)
                    .build();
    
    return bbsMapper.insertBbs(bbs);
   
    /*
     * 작성화면 클릭 -> 로그인 체크 인터셉터 -> (통과하면) 작성화면 (세션의 user 정보 사용) -> 서버로 넘김
     */
    
  }

  @Override
  public void loadBbsList(HttpServletRequest request, Model model) {
    
    // 전체 BBS 게시글 수
    int total = bbsMapper.getBbsCount();
    
    // 한 화면에 표시할 BBS 게시글 수
    int display = 20;
    
    // 표시할 페이지 번호
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    
    // 페이징 처리에 필요한 정보 처리
    myPageUtils.setPaging(total, display, page);
    
    // DB 로 보낼 Map 생성
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin(), "end", myPageUtils.getEnd());
    
    // DB 에서 목록 가져오기
    List<BbsDto> bbsList = bbsMapper.getBbsList(map);
    
    //1000개  display가 기본이 20개. page = 1, beginNo 1000 page = 2, beginNo 980
    
    
    // 뷰로 전달한 데이터를 Model 에 저장
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("bbsList", bbsList);
    model.addAttribute("paging", myPageUtils.getPaging(request.getContextPath() + "/bbs/list.do"
                                                   , null
                                                   , display));
    // 원래 해놨던 sort가 1번만 정렬이 가능한 구조여서 (계층형은 기본정렬이 2번임) 약간 커스터마이징을 했음(MyPageUtils)
    
    
    
    
  }

  @Override
  public int registerReply(HttpServletRequest request) {

    // 요청 파라미터
    // 답글 정보 : userNo, contents
    // 원글 정보 : depth, groupNo, groupOrder
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    String contents = MySecurityUtils.getPreventXss(request.getParameter("contents"));
    int depth = Integer.parseInt(request.getParameter("depth"));
    int groupNo = Integer.parseInt(request.getParameter("groupNo"));
    int groupOrder = Integer.parseInt(request.getParameter("groupOrder"));
    
    
    // 원글 BbsDto 객체 생성
    BbsDto bbs = BbsDto.builder()
                  .depth(depth)
                  .groupNo(groupNo)
                  .groupOrder(groupOrder)
                  .build();
    
    // 기존 답글들의 groupOrder 업데이트
    bbsMapper.uadateGroupOrder(bbs); 
    // updateCount(업데이트 된 행의 개수)가 쓸모가 없어진다. 개수가 몇개가 나올지 모르게되니까 숫자가 1인지 뭔지는 의미없다. 첫 답글인 경우에만 "0" 이 떨어진다.
    
    
    
    // 답글 BbsDto 객체 생성
    UserDto user = new UserDto();
    user.setUserNo(userNo);
    BbsDto reply = BbsDto.builder()
                     .user(user)
                     .contents(contents)
                     .depth(depth + 1)
                     .groupNo(groupNo)
                     .groupOrder(groupOrder + 1)
                 .build();
    
    // 새 답글의 추가
    return  bbsMapper.insertReply(reply); // insert는 성공하면 1이다. 이걸 반환값으로 사용함.
  }

  @Override
  public int removeBbs(int bbsNo) {
    return bbsMapper.removeBbs(bbsNo);
  }

  @Override
  public void loadBbsSearchList(HttpServletRequest request, Model model) {
    
    // 요청 파라미터
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    // 검색 데이터 개수를 구할 때 사용할 Map 생성
    Map<String, Object> map = new HashMap<String, Object>(); // 여기선 of.Map 으로 만드는게 아니라 기초적으로 만들어 필요할때마다 저장해주는 방식을 써야 편하다.
    map.put("column", column);
    map.put("query", query);

    // 검색 데이터 개수 구하기
    int total = bbsMapper.getSearchCount(map);
    
    // 한 페이지에 표시할 검색 데이터 개수
    int display = 2;
    
    // 현재 페이지 번호
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    
    // 페이징 처리에 필요한 처리
    myPageUtils.setPaging(total, display, page);
    
    // 검색 목록을 가져오기 위해서 기존 Map 에 begin 과 end 를 추가
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    // 검색 목록 가져오기
    List<BbsDto> bbsList = bbsMapper.getSearchList(map);
    
    // 뷰로 전달할 데이터
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("bbsList", bbsList);
    model.addAttribute("paging", myPageUtils.getPaging(request.getContextPath() + "/bbs/search.do"
                                                     , ""
                                                     , 20
                                                     , "column=" + column + "&query=" + query));
    // 메소드 오버로딩으로 getPaging 을 여러개 만들어 놓고 상황맞춰 가져다 써도 된다. 추천추천 EX)아 sort 필요 없는데;;
    
    
 
  }
  

  
}
