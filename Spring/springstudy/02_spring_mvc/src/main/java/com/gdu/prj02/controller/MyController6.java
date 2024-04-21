package com.gdu.prj02.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.gdu.prj02.dto.UserDto;

@SessionAttributes(names="user")  // Model 에 user 가 저장되면 session 에 같은 값을 저장한다.
@Controller
public class MyController6 {

  @GetMapping("/user/login1.do")
  public String login1(HttpServletRequest request) {
    
    // HttpSession 구하기
    HttpSession session = request.getSession();
    
    // session 에 저장할 객체
    UserDto user = new UserDto(1, "min@naver.com");
    
    // session 에 객체 저장하기
    session.setAttribute("user", user);
    
    // 메인 페이지로 이동
    return "redirect:/main.do";  // 로그인이 끝나면 새로운 요청으로 끝나는게 일반적. index -> forward /main.do -> redirect
  }
  
  @GetMapping("/user/logout1.do")
  public String logout1(HttpServletRequest request) {
    // HttpSession 구하기
    HttpSession session = request.getSession();
    
    // session 의 모든 정보 지우기
    session.invalidate();
    
    // 메인 페이지로 이동
    return "redirect:/main.do";
  }
  
  @GetMapping("/user/login2.do") 
  public String login2(Model model) {
    
    // model 에 저장할 객체
    UserDto user = new UserDto(1, "min@naver.com");
    
    // model 에 객체 저장하기 (@SessionAttributes 에 의해서 session 에도 저장된다.)
    model.addAttribute("user", user);
    
    return "redirect:/main.do";
  }
  
  @GetMapping("/user/logout2.do")
  public String logout2(SessionStatus sessionStatus) {
    
    // session attribute 삭제를 위해 세션 완료 처리 (위 로그아웃1의 invalidate와는 다른 메소드!)
     sessionStatus.setComplete();
    
    return "redirect:/main.do";
    
    // 오 이거..로그인 2로 하면 로그아웃 2로 밖에 로그아웃 안되는데 로그인 1하면 로그아웃 2로도 가능함. setComplete가 뭔가 더 상위인가
    // 참고로 이거 그냥 세션이 어떻게 정보 저장해주는 지 보는거예요. 진짜 로그인 로그아웃 이렇게 안해요!!
    // 지금 핵심은 각각의 데이터저장이 어떻게 이루어지나 보는거임!!
  }
  
  //@GetMapping("/user/mypage.do")
  public String mypage1(HttpSession session, Model model) {   //request.getSession 할 필요 없이 바로 가져올 수 있음. 스프링dms 쌉가넝
    
    
    // session 에 저장된 user 정보
    UserDto user = (UserDto) session.getAttribute("user");      
    
    // model 에 user 정보 저장
    model.addAttribute("user", user);                         // 지금 모델이 정보 가져가고 있음!
    
    // user/mypage.jsp 로 forward
    return "user/mypage";
  }
  
  @GetMapping("/user/mypage.do")
  public String mypage2(@SessionAttribute(name = "user") UserDto user) { //s 없는거 주의!! session attribute 중 user 를 UserDto user에 저장하시오.

    // 아니 이렇게 코드가 없는데 동작한다고? ->  SessionAttribute "user" 찾아서 커맨드 객체에 저장한다. 커맨드 객체는 자동으로 모델에 저장된다.
    // 그래서 멀쩡히 작동한다.
    // 머리가 터질 것 같나요? 일단 세션 정보 어떻게나 확인하는 정도예요...로그인도 마이페이지도 실제로 이렇게 구현하지 않습니다. 지금 세션에서 정보 꺼내는거 배우는거~
   
    // user/mypage.jsp 로 forward
    return "user/mypage";
  }
  
}
