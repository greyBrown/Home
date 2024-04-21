package com.gdu.myapp.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gdu.myapp.dto.UserDto;
import com.gdu.myapp.mapper.UserMapper;
import com.gdu.myapp.utils.MyJavaMailUtils;
import com.gdu.myapp.utils.MySecurityUtils;

@Service
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  //원래 인터페이스는 객체생성이 안되지만 마이바티스 업그레이드로 사용자 편의를 위해 지원된다.
  
  private final MyJavaMailUtils myJavaMailUtils;
  
  private String clientId = "XeShO8IjERM4jJY5KFwX";
  private String clientSecret = "pjz2dxpwH2";
  
  // @autoweird 는 생략. 생성자를 직접 만들었으니까
  public UserServiceImpl(UserMapper userMapper, MyJavaMailUtils myJavaMailUtils) {
    super();
    this.userMapper = userMapper;
    this.myJavaMailUtils = myJavaMailUtils;
  }

  @Override
  public void signin(HttpServletRequest request, HttpServletResponse response) {
    
    try {  //try 할때 애매하면 싹다 집어넣기 ^-^
      
      // 입력한 아이디
      String email = request.getParameter("email");
      
      // 입력한 비밀번호 + SHA-256 방식의 암호화
      String pw = MySecurityUtils.getSha256(request.getParameter("pw"));
      
      // 접속 IP(접속 기록을 남길 때 필요한 정보)
      String ip = request.getRemoteAddr();
      
      // 접속 수단 (요청 헤더의 User-Agent 값)
      String userAgent = request.getHeader("User-Agent");
      
      
      // DB로 보낼 정보  (email/pw: USER_T , email/ip/userAgent/sessionId: ACCESS_HISTORY_T) 
      Map<String, Object> params = Map.of("email", email
                                        , "pw", pw
                                        , "ip", ip
                                        ,"userAgent", request.getHeader("User-Agent")   // 세션아이디 기록을 위한 코드추가
                                        ,"sessionId", request.getSession().getId());                              
          
      
      // email/pw 가 일치하는 회원 정보 가져오기
      UserDto user = userMapper.getUserByMap(params);
      
      // 일치하는 회원 있음(Sign In 성공)
      if(user != null) {
        // 접속 기록 ACCESS_HISTORY_T 에 남기기
        userMapper.insertAccessHistory(params);
        // 회원 정보를 세션(브라우저 닫기 전까지 정보가 유지되는 공간, 기본 30분 정보 유지)에 보관하기
        // 이런 session 등에 저장한 데이터명 같은거 팀원 전부가 잘 공유해야 한다. 시작할때 정하고 시작함.
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("user", user);
      //session.setMaxInactiveInterval(10); // 세션 유지 시간 10초 설정.
        
       // Sign In 후 페이지 이동
        response.sendRedirect(request.getParameter("url"));
      // 일치하는 회원 없음 (Sign In 실패)
      } else {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("alert('일치하는 회원 정보가 없습니다.')");
        out.println("location.href='" + request.getContextPath() + "/main.page'");
        out.println("</script>");
        out.flush();
        out.close();
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }

  @Override
  public ResponseEntity<Map<String, Object>> checkEmail(Map<String, Object> params) {
    boolean enableEmail = userMapper.getUserByMap(params) == null
                       && userMapper.getLeaveUserByMap(params) == null;
    return new ResponseEntity<>(Map.of("enableEmail", enableEmail), HttpStatus.OK);
  } //계속 언급되는 것 : 응답도 JSON으로 만드는데 이 JSON 대신 만드는게 ResponseEntity. 이 데이터가 fetch로 넘어간다.
  
  
  @Override
  public ResponseEntity<Map<String, Object>> sendCode(Map<String, Object> params) {
    
    /*
     * 구글 앱 비밀번호 설정 방법
     * 1. 구글에 로그인한다.
     * 2. [계정] - [보안]
     * 3. [Google에 로그인하는 방법] - [2단계 인증]을 사용 설정한다.
     * 4. 검색란에 "앱 비밀번호"를 검색한다.
     * 5. 앱 이름을 "myapp"으로 작성하고 [만들기] 버튼을 클릭한다.
     * 6. 16자리 비밀번호가 나타나면 복사해서 사용한다. (비밀번호 사이 공백은 모두 제거한다.)
     */
    
    
    // 인증코드 생성
    String code = MySecurityUtils.getRandomString(6, true, true);  // 둘 다 false만 아니면 됨 ㅎㅎ 
    
    // 개발할 때 인증코드 찍어보기
    System.out.println(code);
    
    // 메일 보내기
    myJavaMailUtils.sendMail((String)params.get("email")
                           ,"myapp 인증요청" 
                           , "<div>인증코드는 <strong>" + code + "</strong> 입니다.");
    
    // 인증코드 입력화면으로 보내주는 값
    return new ResponseEntity<>(Map.of("code", code), HttpStatus.OK);
  }
  
  @Override
  public void signup(HttpServletRequest request, HttpServletResponse response) {

    // 전달된 파라미터
    String email = request.getParameter("email");
    String pw = MySecurityUtils.getSha256(request.getParameter("pw"));
    String name = MySecurityUtils.getPreventXss(request.getParameter("name"));
    String mobile = request.getParameter("mobile");
    String gender = request.getParameter("gender");
    String event = request.getParameter("event");
    
    // Mapper 로 보낼 UserDto 객체 생성
    UserDto user = UserDto.builder()
                    .email(email)
                    .pw(pw)
                    .name(name)
                    .mobile(mobile)
                    .gender(gender)
                    .eventAgree(event == null ? 0 : 1)
                    .build();
    
    // 회원 가입
    int insertCount = userMapper.insertUser(user);
    
    // 가입 후 로그인을 시켜줄 것이냐, 로그인 화면으로 안내해줄 것이냐 택 1. 
    //로그인페이지로 보냈을때 회원가입페이지(referer)로 돌아가지 않게 조심... 이런거 신경쓰기 싫으면 메인으로 보내버리는 방법 권장함
    
    // 응답 만들기 (성공하면 sign in 처리하고 main.do 이동, 실패하면 뒤로가기)
    response.setContentType("text/html; charset=UTF-8");
    try {
      PrintWriter out =  response.getWriter();
      out.println("<script>");
      // 가입 성공
      if(insertCount == 1) {
        
        // Sign In 및 접속 기록을 위한 Map
        Map<String, Object> map = Map.of("email", email, "pw", pw, "ip", request.getRemoteAddr()
                                          ,"userAgent", request.getHeader("User-Agent")   // 세션아이디 기록을 위한 코드추가
                                          ,"sessionId", request.getSession().getId());
        
        // Sign in(세션에 user 저장하기)
        request.getSession().setAttribute("user", userMapper.getUserByMap(map));
        
        // 접속 기록 남기기
        userMapper.insertAccessHistory(map);
        
        out.println("alert('회원가입이 완료되었습니다..')");
        out.println("location.href='"+request.getContextPath() +"/main.page'");
      
        // 가입 실패
      } else {
        out.println("alert('회원가입에 실패하였습니다.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
    

  
  @Override
  public void signout(HttpServletRequest request, HttpServletResponse response) {
  try {
      
      // Sign Out 기록 남기기
      HttpSession session = request.getSession();
      String sessionId = session.getId(); 
      userMapper.updateAccessHistory(sessionId);
      
      // 세션에 저장된 모든 정보 초기화
      session.invalidate();
      
      // 메인 페이지로 이동
      response.sendRedirect(request.getContextPath() + "/main.page");
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }

  


  @Override
  public void leave(HttpServletRequest request, HttpServletResponse response) {
     
    try {
      // 세션에 저장된 user 값 확인
      HttpSession session = request.getSession();
      UserDto user = (UserDto) session.getAttribute("user");
      
      // 세션 만료로 user 정보가 세션에 없을 수 있음(그렇다 그럴 수도 있다)
      if(user == null) {
        response.sendRedirect(request.getContextPath() + "/main.page");
      }
      
      // 탈퇴 처리
      
      int deleteCount = userMapper.deleteUser(user.getUserNo());
      // 여기서 userNo 안빼고 user 그대로만 넘겨도 mapper 측에서는 알아서 빼서 쓴다. 하지만 빼보겠음. 보통빼더라고요? 그러게요 저도...
      
      // 탈퇴 이후 응답 만들기
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("<script>");

      // 탈퇴 성공
      if(deleteCount == 1) {
        
        // 세션에 저장된 모든 정보 초기화
       session.invalidate(); // 세션초기화. 로그아웃시켜줘야한다. SessionStatus 객체의 setComplete() 메소드 호출
        // session 잘 모르겠으면 02_MVC 공부를 열심히 해봅니다....
  
        
        out.println("alert('탈퇴되었습니다. 이용해 주셔서 감사합니다.');");
        out.println("location.href='" + request.getContextPath() + "/main.page';");
        out.println();
      
       // 탈퇴 실패
      } else {
        out.println("alert('탈퇴되지 않았습니다.');");
        out.println("history.back();");
      }
      out.println("</script>");
    } catch (Exception e) {
      e.printStackTrace();
    } 
    
  }
  
 
  
  @Override
  public String getRedirectURLAfterSignin(HttpServletRequest request) {
    
    // 로그인 할 당시 있었던 화면으로 로그인 후에 돌려보내줘야 한다. 이전페이지 주소를 알아내 보내준다 -> request의 header 값에 이전주소값이 들어있다.
    //Sign In 페이지 이전의 주소가 저장되어 있는 Request Header 의 referer
    String referer = request.getHeader("referer");
    
    // referer 로 돌아가면 안 되는 예외 상황 (아이디 찾기 화면, 비밀번호 찾기 화면, 가입화면... 사용자가 원하는 '이전페이지'가 아님)
    String[] excludeUrls = {"/findId.page", "/findPw.page", "/signup.page"}; // 그런 예외사항 주소들 하나씩 배열에 집어넣어 준다.
    
    // Sign In 이후 이동할 url
    String url = referer;
    if(referer != null) {                       // 사이트 오자마자 로그인 = referer이 null 값임 -> 그럼 로그인하면 메인페이지로 가게 하기
      for(String excludeUrl : excludeUrls) {
        if(referer.contains(excludeUrl)) {  //excludeUrls 들은 메인페이지로 보낸다.
          url =  request.getContextPath() +  "/main.page";
          break;
        }
      }
    } else {
      url = request.getContextPath() + "/main.page";         // referer이 없으면 메인페이지로 보낸다.
    }
    
    // 즉 기본적으로 url = referer 이지만 예외상황인 경우에만 url을 main으로 변경하겠다.
    
    return url;
    
  }
  
  @Override
  public String getNaverLoginURL(HttpServletRequest request) {
    

    /******************* 네이버 로그인 1********************/
    String redirectUri = "http://localhost:8080" + request.getContextPath() + "/user/naver/getAccessToken.do";
    String state = new BigInteger(130, new SecureRandom()).toString();
    // state 이렇게 뽑으라고 개발자센터 메뉴얼에 나온다
    
    StringBuilder builder = new StringBuilder();
    builder.append("https://nid.naver.com/oauth2.0/authorize");
    builder.append("?response_type=code");
    builder.append("&client_id=" + clientId);
    builder.append("&redirect_uri=" + redirectUri);
    // 네이버 로그인 2단계에서 어느 주소로 접속할건지(redirect_url) 그 주소를 알려달라. 그 주소가 맞으면 해주고 아니면 안해주겠다.
    // 이후 토큰으로도 검증함. 너가 준 토큰이랑 내가 발급한 토큰이랑 맞으면 너라고 인증해주겠다.
    // 네이버 로그인 Callback URL  <<< 사용하기로 한 주소를 등록해줌. 크게 두가지 토큰 받는거 프로필(고객이 동의한 개인정보) 받는거
    // 우리 이 주소로 너희한테 정보 요청할거다. 그 주소 등록. localhost:8080 이 부분은 나중에 배포할 때 싹 들어내고 바꿔야 함
    builder.append("&state=" + state);
    
    return builder.toString();
  }
  
  @Override
  public String getNaverLoginAccessToken(HttpServletRequest request) {
    /******************* 네이버 로그인 2************************/
    // 네이버로부터 Access Token 을 발급받아 반환하는 메소드
    // 네이버 로그인 1단계에서 전달한 redirect_uri 에서 동작하는 서비스
    // code 와 state 파라미터를 받아서 Access Token을 발급 받을 때 사용
    
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    
    String spec = "https://nid.naver.com/oauth2.0/token";
    String grantType = "authorization_code";
    // 아이디랑 비번 계속 나와서 걍 필드로 뺌 
    
   
    
    StringBuilder builder = new StringBuilder();
    builder.append(spec);
    builder.append("?grant_type=" + grantType);
    builder.append("&client_id=" + clientId);
    builder.append("&client_secret=" + clientSecret);
    builder.append("&code=" + code);
    builder.append("&state=" + state);
    
    HttpURLConnection con = null;
    JSONObject obj = null; // try 영역 바깥으로 scope를 잡아줘야 무사히 return까지 도달하니까 바깥쪽에 선언. 이걸 return 하는게 목적!
    
    try {
      
      // 요청
      URL url = new URL(builder.toString());
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");   // 반드시 대문자로 작성해야 함.(필드값이 모두 대문자로 작성되어 있기 때문)
      
      // 응답 스트림 생성
      BufferedReader reader = null;
      int responseCode = con.getResponseCode();
      if(responseCode == HttpURLConnection.HTTP_OK) {
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {
        reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));  //정상스트림은 검정색, 에러스트림은 빨간색으로 이클립스에서 구분됨. 에러 안나길 바라지만 그래도 추가해놓음....
      }
      // 네이버->자바(나) => 즉 읽어들이는 input 임. input은 원래 byte를 읽어오는데 네이버에서는 txt를 제공하니까 byte를 char로 바꿔주던 inputstreamreader가 필요함(tmi 햇었음)
      // 최종적인 모양새 (buffer(inputstreamareader(inputstream))) 익숙한 모양새죱
      
      // 응답 데이터 받기
      String line = null;
      StringBuilder responseBody = new StringBuilder();
      while((line = reader.readLine()) != null){
        responseBody.append(line);
      }
      
      // 응답 데이터를 JSON 객체로 변환하기(이걸 위해 디펜던스 추가함!)
      obj = new JSONObject(responseBody.toString());  
      
      // 응답 스트림 닫기
      reader.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    con.disconnect();
   
    
    return obj.getString("access_token");
 // 요게 네이버가 주는 접근토큰. 이걸 controller로 반환시켜주는게 이 메소드의 목적! 그리고 컨트롤러가 이걸 가지고 사용자정보를 다시 요청함. 정보받기 여간 힘든게 아니다...
  }
  
  @Override
  public UserDto getNaverLoginProfile(String accessToken) {
    /******************* 네이버 로그인 3************************/
    // 네이버로부터 프로필 정보(이메일, [이름, 성별, 휴대전화번호]) 을 발급받아 반환하는 메소드
    // 이렇게 sns로 가입을 하면 비밀번호가 없어서...마이페이지도 그렇고 여러모로 곤란한 일이 생김(비밀번호 있는 사람 없는 사람이 섞여있는 상태)
    // -> 그래서 네이버로 가입하면 간편가입 창을 띄워서 비밀번호를 강제로 만들기도 하고...네이버로그인되어 있는 상태면 아예 마이페이지가 안뜨게 하기도 하고...여러 방법이 있음
    
    String spec = "https://openapi.naver.com/v1/nid/me";
    
    HttpURLConnection con = null;
    UserDto user = null;
    
    try {
    
      // 요청
      URL url = new URL(spec);
      con = (HttpURLConnection)url.openConnection();
      con.setRequestMethod("GET");
      
      // 요청 헤더
      con.setRequestProperty("Authorization", "Bearer " + accessToken); // 이 메소드가 요청 헤더 만들 때 쓰는 자바 메소드. 앞에 헤더이름 뒤엔 값
      // "Authorization: Bearer Aamdsn뭐시기뭐시기토큰rc="   <- 요 모양을 만들어 준 것임
      
      // 응답 스트림 생성
      BufferedReader reader = null;
      int responseCode = con.getResponseCode();
      if(responseCode == HttpURLConnection.HTTP_OK) {
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {
        reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));  //정상스트림은 검정색, 에러스트림은 빨간색으로 이클립스에서 구분됨. 에러 안나길 바라지만 그래도 추가해놓음....
      }
      
      // 응답 데이터 받기
      String line = null;
      StringBuilder responseBody = new StringBuilder();
      while((line = reader.readLine()) != null){
        responseBody.append(line);
      }
      
      /* 지금 JSON 에서 이렇게 생겼고 이걸 이렇게 가져오는중....
       * {
       *   "resultcode": xx,
       *   "message": xx,
       *   "response": { //객체가 들어있다
       *      "id":식별자//네이버 아이디 아님...
       *      "name":xx,....
       *    }  
       * }
       * -------------------------------------------------------
       * JSONObject obj = new JSONObkect(responseBody.toString());
       * JSONObject response = obj.getJSONObject("response");
       * String name = response.getString("name");
       */
      
      
      // 응답 데이터를 JSON 객체로 변환하기(이걸 위해 디펜던스 추가함!)
      JSONObject obj = new JSONObject(responseBody.toString());  
      JSONObject response = obj.getJSONObject("response");
      user = UserDto.builder()
                 .email(response.getString("email"))
                 .gender(response.has("gender") ? response.getString("gender") : null)
                 .gender(response.has("mobile") ? response.getString("mobile") : null)
              .build();
      
      if(response.has("name")) user.setName(response.getString("name"));
      // 필수 정보 외에는 존재하지 않을 수도 있음. 이메일만 필수임! 아래처럼 1)if로 한다 2)삼항연산자로 한다.  등등의 처리를 할 수 있음. 있을때만 넣어주겠는 뜻
      
      // 응답 스트림 닫기
      reader.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    con.disconnect();
    
    return user;
    
  }
  
  @Override
  public boolean hasUser(UserDto user) {

    
    return userMapper.getUserByMap(Map.of("email", user.getEmail())) != null;   //null이 아니면 존재한다(true)
  }

  @Override
    public void naverSignin(HttpServletRequest request, UserDto naverUser ) {
   
    // 네이버 로그인으로만 들어오는 상황. 네이버버튼을 누른 사람들. (즉 비밀번호 없이 이메일로만 로그인됨). 나중에 이 비밀번호 관련해 처리를 해줘야 한다.
    Map<String, Object> map = Map.of("email", naverUser.getEmail(),
                                     "ip", request.getRemoteAddr());
    
    UserDto user = userMapper.getUserByMap(map);
    request.getSession().setAttribute("user", user);
    userMapper.insertAccessHistory(map);
    
    }
}
