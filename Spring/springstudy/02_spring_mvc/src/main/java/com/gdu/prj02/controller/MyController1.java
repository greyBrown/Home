package com.gdu.prj02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller  //컨트롤러
public class MyController1 {
  
  /*
   * 메소드
   * 
   * 1. 반환타입
   *  1) String : 응답할 jsp 의 경로와 이름을 반환한다.
   *  2) void
   *    (1) 요청한 주소를 jsp 의 경로와 이름으로 인식하고 처리한다.
   *        - 요청주소 : /board/list.brd
   *        - jsp 경로 : /board/list.jsp
   *    (2) 직접 응답(HttpServletResponse)을 만든다. 대부분 JavaScript 코드를 만든다.
   *    
   * 2. 메소드명
   *  아무 일도 안 한다.
   *  
   * 3. 매개변수
   *  1) 요청과 응답을 위한 각종 변수의 선언이 가능하다.
   *  2) 주요 매개변수(-> 서블렛의 주요 두 객체를 쓸 수 있다! 여기서 추가로 쓸 수 있는 객체들이 생긴다)
   *    (1) HttpServletRequest request
   *    (2) HttpServletResponse response
   *    (3) HttpSession session
   *    (4) 커맨드 객체 : 요청 파라미터를 받는 객체
   *    (5) 일반 변수   : 요청 파라미터를 받는 변수
   *    (6) Model model : forward 할 때 정보를 저장할 객체(attribute)
   *    (7) RedirectAttribute rttr : redirect 할 때 정보를 저장할 객체(flash attribute) (Redirect일 때 (flash)attribute를 전달할 수 있다. 무려 이게 된다...!! 스프링이 지원함)
   */
  
  
  // value="/" : contextPath 요청을 의미한다. http://localhost:8080.prj02
  // value="/main.do" : contextPath/main.do 요청. http://localhost:8080/prj02/main.do
  // 둘 다 적어놓고 싶으면? {배열}을 사용한다. 그러니까...암것도 없으면 메인으로 가용. 메인으로 치면 메인으로 가용. 이런 맵핑!! 잘 보자 이건 Mapping임!
  // 잘 생각해보면 이런 홈페이지들이 있다. 그냥 들어가서 주소가 없으면 메인, 메인을 쳐도 메인!
  @RequestMapping(value={"/", "/main.do"}, method=RequestMethod.GET)
  public String welcome() {
    // 뷰리졸버 prefix : /WEB-INF/views/
    // 뷰리졸버 suffix : .jsp
    // 실제리턴        : /WEB-INF/views/index.jsp
    return "index";
  }
  
  
  
  
  
  
}
