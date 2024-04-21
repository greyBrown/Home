package com.gdu.prj09.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.gdu.prj09.dto.MemberDto;

public interface MemberService {

  // 하나의 service는 여러개의 dao 를 부를 수 있다. 글쿤...
  
  ResponseEntity<Map<String, Object>> getMembers(int page, int display);           //spa 작업할 떄 반환하는 타입으로 쓰라고 spring에서 만들어줌. @Responsebody를 가지고 있는 클래스
  
  // 오늘 주소 체계 약간 다름. /prj09/members/page/1/display/20 이런식으로 주소 나오게 할 거임. ?page=1&display=20 이 아니라! 좀 다른 방식 소개
  // -> 파라미터가 아니라 경로임. 이런 방식의 데이터처리는 @PathVariable 이라고 함. 매개변수로는 int page, int display 받아오기
  /*
   * /members -----> getMembers(int page, int display) -----> getTotalMemberCount()
   *                                                          getMemberList(Map map)
   */
  
  ResponseEntity<Map<String, Object>> getMemberByNo(int memberNo);
  //주소 모양은 members/1  <- member 1번
  
  ResponseEntity<Map<String, Object>> registerMember(Map<String, Object> map, HttpServletResponse response);
  //1(성공) 혹은 0(실패) 이 온다고 하더라도 jackson 라이브러리가 json으로 바꿔주도록 하기 위해 map을 사용.
  // DB의 unique 처리에 대해 생각해봐야 함. DAO에서 중복체크를 해주는 메소드가 하나 있다던가...이런식으로 중복체크를 할 수 있음.
  // 하지만 오늘은 중복체크 안할 거예요!! 그냥 넣을거에요. 
  //오늘의 구현방향은 그냥 우겨넣고 1. 정상 처리 2. Excep 발생 -> 예외처리(try-catch)로 응답(response) 만들어줌. 이렇게 처리해 볼 것임.
  
  ResponseEntity<Map<String, Object>> modifyMember(Map<String, Object> map);
  // 이메일은 변경 못하게 할거라서 response는 필요 없음. 이메일이 회원아이디
  
  ResponseEntity<Map<String, Object>> removeMember(int memberNo);
  // /mebers/1 이렇게 뜨는데 이게 1번 멤버의 상세조회인지 삭제인지 어떻게 구별하는가 ? 
  // method가 추가되면서 상세보기와 삭제가 같은 주소를 쓰지만 다르게 동작할 것임
 // 마찬가지로 @PathVariable 로 받을거니까 int memberNo 만.
  
  ResponseEntity<Map<String, Object>> removeMembers(String memberNoList);
  // 삭제할 멤버 여러명 일때...어떻게 할지 -> 이렇게 할 예정 /members/1, 2, 3 -> 즉 String을 넘어옴.
  
  
  
  
  
  
}
