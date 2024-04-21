package com.gdu.myapp.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.myapp.dto.UserDto;
import com.gdu.myapp.service.UserService;

@RequestMapping("/user")
@Controller
public class UserController {

  private final UserService userService;
 
  // @Autowired 생략
  public UserController(UserService userService) {
    super();
    this.userService = userService;
  }
  
  @GetMapping("/signup.page")
  public String signupPage() {
    return "user/signup";
  }
  
  @GetMapping(value="/signin.page")
  public String signinPage(HttpServletRequest request
                          , Model model) {

    
    // Sign In 페이지로 url 넘겨 주기 (로그인 후 이동할 경로를 의미함)
    model.addAttribute("url",  userService.getRedirectURLAfterSignin(request));
    
    // Sign In 페이지로 naverLoginURL 넘겨 주기 (네이버 로그인 요청 주소를 의미함)
    model.addAttribute("naverLoginURL", userService.getNaverLoginURL(request));
    // 이 model에 저장된 정보는 jsp로 포워딩 된다. signin 페이지에서 naverLoginUrl를 EL로 확인할 수 있어짐.
    
    
    return "user/signin"; 
  }
  
  @PostMapping("/signin.do")
  public void signin(HttpServletRequest request, HttpServletResponse response) {
    userService.signin(request, response);
  }
  
  @GetMapping("/naver/getAccessToken.do")
  public String getAccessToken(HttpServletRequest request) {
    String accessToken = userService.getNaverLoginAccessToken(request);
    // 토큰 주고받고 사용자 정보요청을 한다. 고객님 프로필...주세요.....
    
    
    return  "redirect:/user/naver/getProfile.do?accessToken=" + accessToken; //accessToken 파라미터 이렇게 썻으니 아래에서도 맞춰줘야 함
  }
  
  // 위에서 토큰을 받고 바로 여기로 넘어옴
  @GetMapping("/naver/getProfile.do")
  public String getProfile(HttpServletRequest request, Model model) {
    // 이동경로가 갈린다. 간편가입을 진행해야 하는 사람. 이미 가입이되어 있어 이제 들어가면 되는 사람.
    
    // 네이버로부터 받은 프로필 정보
    UserDto naverUser = userService.getNaverLoginProfile(request.getParameter("accessToken"));
    
    // 반환 경로
    String path = null;
    
    // 프로필이 DB에 있는지 확인 (있으면 Sign In, 없으면 Sign Up)
    if(userService.hasUser(naverUser)) {
      // Sign In - 리다이렉트
      userService.naverSignin(request, naverUser);
       path = "redirect:/main.page";
    } else {
      // Sign up (네이버 가입 화면으로 이동) - 포워드 (JSP로 정보 모델 때 model 사용)
      model.addAttribute("naverUser", naverUser);
      path = "user/naver_signup";
    }
    
    return path;
  }
  
  @PostMapping(value="/checkEmail.do", produces="application/json")
  public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody Map<String, Object> params){
   
   return userService.checkEmail(params); 
    
   // 리턴값 잘 나오는지 먼저 확인...return new ResponseEntity<>(HttpStatus.OK); // 요렇게 생략한 모습으로 넘길 수 있음. generic들은 생성시점의 타입은 생략할 수 있다.
  }
  
  @PostMapping(value="/sendCode.do", produces="application/json")
  public ResponseEntity<Map<String, Object>> sendCode(@RequestBody Map<String, Object> params){
   return userService.sendCode(params);
  }
  
  @PostMapping("/signup.do")
  public void signup(HttpServletRequest request, HttpServletResponse response) {
    userService.signup(request, response);
  }
  
  @GetMapping("/leave.do")
  public void leave(HttpServletRequest request, HttpServletResponse response) {
   // 1. request로 session 가져오기 2. HttpSession 바로 선언하기 3. @SessionAttribute(name="user") UserDto user로 가져오기
   // 2번의 경우 UserDto user = (UserDto) session.getAttribute("user"); 이렇게~ 난 1번과 2번의 혼종을 썼구나....
   // 여기선 첫번째 껄로 합니다. 3번 궁금한데 나중에 한번 해보기
    
    userService.leave(request, response);
  }
  
  @GetMapping("/signout.do")
  public void signout(HttpServletRequest request, HttpServletResponse response) {
    userService.signout(request, response);
  }
  
  
  
}
