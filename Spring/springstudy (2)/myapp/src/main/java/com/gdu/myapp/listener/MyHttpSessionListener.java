package com.gdu.myapp.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gdu.myapp.mapper.UserMapper;

public class MyHttpSessionListener implements HttpSessionListener {

  @Autowired
  private UserMapper userMapper;
  
  @Override
  public void sessionCreated(HttpSessionEvent se) {

    HttpSession session = se.getSession();
    String sessionId = session.getId();
    
    System.out.println(sessionId + " 세션 정보가 생성되었습니다.");
    
  }
  
  // 세션 만료 시 자동으로 동작
  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
    
    // Sign Out 기록 남기기
    
    // HttpSession
    HttpSession session = se.getSession();
    
    // ApplicationContext(servletContext를 jsp 에서는 ApplicationContext 라고 불렀음. 가장 넓은 lifeCycle). JSP Study에 있음
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
    
    // session_id
    String sessionId = session.getId();
    
    // getBean()
    // Service 를 추천하나, Mapper 도 가능
    UserMapper userMapper = ctx.getBean("userMapper", UserMapper.class); // IoC에 저장된 이름 UserMapper, 타입 UserMapper
    
    // updateAccessHistory()
    userMapper.updateAccessHistory(sessionId);

    // 확인 메시지
    System.out.println(sessionId + " 세션 정보가 소멸되었습니다.");
    
    // web.xml 에 가서 MyHttpSessionListener 추가해준다.
    
  }
  
}