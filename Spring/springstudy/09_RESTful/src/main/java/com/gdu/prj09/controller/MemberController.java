package com.gdu.prj09.controller;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gdu.prj09.service.MemberService;

import lombok.RequiredArgsConstructor;

/*
 * RESTful
 * 1. Representation State Transfer
 * 2. 요청 주소를 작성하는 한 방식이다.
 * 3. 요청 파라미터를 ? 뒤에 추가하는 Query String 방식을 사용하지 않는다.
 * 4. 요청 파라미터를 주소에 포함하는 Path Variable 방식을 사용하거나, 요청 본문에 포함하는 방식을 사용한다.
 * 5. 요청의 구분을 "주소 + 메소드" 조합으로 구성한다.
 * 6. CRUD 요청 예시
 *              URL                        |  Method 
 *  1) 목록    /members                    |  GET
 *             /members/page1              |    
 *             /members/page1/display/20   |
 *  2) 상세    /members/1                  |  GET 
 *  3) 삽입    /members                    |  POST
 *  4) 수정    /members                    |  PUT
 *  4) 삭제    /member/1                   |  DELETE
 *             /members/1,2,3              |
 *             
 *  // 지난번에 form 데이터로 body에 실어 보냈다면 이번에는 json으로 만들것임. json이 더 쉬운 면도 있음
 *  // 방식이 삽입이면 POST 수정이면 PUT. 메소드(dao와 service 내부의..?)가 다르니까 방식(Method)이 바뀜. 하지만 내부동작은 POST와 유사함. 구분을 위해 이름이 다르다.
 *  // 주소는 같지만 Method 에서 구분이 된다.
 *  
 */            


@RequiredArgsConstructor
@Controller
public class MemberController {
  
   private final MemberService memberService;
   
   @GetMapping("/admin/member.do")
   public void adminMember() {        
     //반환타입이 void 인 경우  주소를 ("/admin/member.do")를 JSP 경로로 인식한다.
     // /admin/member.do   =====> /WEB-INF/views/admin/member.jsp
     
   }
   
   @PostMapping(value="/members", produces="application/json")
   public ResponseEntity<Map<String, Object>> registerMember(@RequestBody Map<String, Object> map,
                                                              HttpServletResponse response){
     // DTO가 쪼개져있어서 JSON데이터를 찢어서 받는게 안되기 때문에....(1. 새로운 DTO를 만든다. 2. MAP으로 받는다) 이렇게 선택지가 나뉠 수 있음
     // 먼저 MAP으로 받는 방법을 연습하고, 최종적으로 새롭게 만들어지는 DTO를 통해 데이터를 받는다.
     
     return memberService.registerMember(map, response);
   }
   
   // @PathVariable을 이용한 mapping 값 value에 {}를 넣는 새로운 문법 사용. 
   // (@PathVariable(value= "page") int page) - > 경로변수로 int page에 "page"를 저장하라. requried 옵션이 있다. (디폴트가 true고 보통 true로 쓴다)
   // 하지만 전달이 안될 경우 null로 인식되기 때문에, 그 대비를 해주는게 베스트. null 값을 대비하는 optional을 사용 
   @GetMapping(value="/members/page/{p}/display/{dp}", produces="application/json")
   public ResponseEntity<Map<String, Object>> getMembers(@PathVariable(value= "p", required=false) Optional<String> optPage
                                                                                            , @PathVariable(value="dp", required=false)Optional<String> optDisplay){
     int page = Integer.parseInt(optPage.orElse("1"));
     int display = Integer.parseInt(optDisplay.orElse("20"));
     return memberService.getMembers(page, display);
   }
   
   @GetMapping(value="/members/{memberNo}", produces="application/json")
   public ResponseEntity<Map<String, Object>> getMemberByNo(@PathVariable(value="memberNo", required=false) Optional<String> opt){
     int memberNo = Integer.parseInt(opt.orElse("0"));
     return memberService.getMemberByNo(memberNo);
   }
   
   @PutMapping(value="/members", produces="application/json") //json 데이터가 body(본문)에 포함되어 있다. 이걸 @RequestBody를 통해 받을 수 있음
   public ResponseEntity<Map<String, Object>> modifyMember(@RequestBody Map<String, Object> map) {
     return memberService.modifyMember(map);
   }
   
   @DeleteMapping(value="/member/{memberNo}", produces="application/json")
   public ResponseEntity<Map<String, Object>> removeMember(@PathVariable(value="memberNo", required=false) Optional<String> opt){
     int memberNo = Integer.parseInt(opt.orElse("0"));
     return memberService.removeMember(memberNo);
   }
   
   @DeleteMapping(value="/members/{memberNoList}", produces="application/json")
   public ResponseEntity<Map<String, Object>> removeMembers(@PathVariable(value="memberNoList", required=false) Optional<String> opt){
     return memberService.removeMembers(opt.orElse("0"));
   }
}
   
  
  /*
   * MyPageUtils(getAsyncPaging) 에서 아래와 같이
   * <a href="javascript:fnPaging(10)"> < </a>
   * <a href="javascript:fnPaging(10)"> 11 </a>
   * <a href="javascript:fnPaging(10)"> 12 </a>
   * <a href="javascript:fnPaging(10)"> 13 </a>
   * <a href="javascript:fnPaging(10)"> > </a>
   * back 에서 이런식으로 페이지를 만들어냄
   * 
   * front 에서는 const fnPaging = (p)=>{
   *                 page=[;
   *                 fnMemberList();
   *                  }                       이런식으로....
   */
  
   /* 1:M 관계의 상세보기는 detail 과 list의 조합이다. 댓글형 게시판과 같은 구조임!!
    * 1 상세보기 하나 띄어주고 그 1이 가진 M의 목록들을 쫙 띄어주는 구조
    * detail과 list
    * 
    */
  
  
  
  
  
  
  
