package com.gdu.prj07.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCkInterceptor implements HandlerInterceptor{
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    
    HttpSession session = request.getSession();
    //어차피 session에는 아무것도 없으니까 굳이 getAttribute 할 필요 없다. 
    // HttpSession 타입으로 하면 String이나 기타 등등으로 형변환 해줄 필요도 없다. 원래 이 타입이기도 하고!
    if(session.getAttribute("user") == null) {
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      out.println("alert('로그인이 필요합니다.')");
      out.println("history.back()");
      out.println("</script>");
      return false;
      
    }
    
    return true;
    
  }
  
}