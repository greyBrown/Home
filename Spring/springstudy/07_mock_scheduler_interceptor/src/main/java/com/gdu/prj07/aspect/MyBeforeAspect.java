package com.gdu.prj07.aspect;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class MyBeforeAspect {

  // PointCut
  @Pointcut("execution (* com.gdu.prj07.controller.*Controller.*(..))")
  public void setPointCut() {}
  
  // Advice
  /*
   * Before Advice 메소드 작성 방법
   * 1. 반환 타입 : void
   * 2. 메소드명  : 마음대로
   * 3. 매개변수  : JoinPoint 타입 객체
   */
  @Before("setPointCut()")
  public void myBeforeAdvice(JoinPoint jointPoint) {
    
    // 요청 메소드/주소/파라미터 로그 남기기
    
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = servletRequestAttributes.getRequest(); // 이 두줄은 getRequest() 를 뽑으려고 한 것
    
   Map<String, String[]> params = request.getParameterMap();      // 요청이 다양해서 그냥 getParameter(단일로 가져옴) 로는 힘듦. 그래서 Parametermap(여러개 한방에 가져옴). 파리미터 Map 의 반환타입은 string string[] 로 정해져 있음 
   
   String str = "";
   if(params.isEmpty()) {      //비어있을지언정 null이 오진 않죠! 그런 구조니까. map이 비었을지언정 null로 오진 않음. 
   
     str += "No parameter";
    
  }else {
    for(Entry<String, String[]> entry : params.entrySet()) {
      str += entry.getKey() + ":" + Arrays.toString(entry.getValue()) + " "; //value가 배열이라 참조값으로 출력됨. 예쁘게 뽑으려고 arrays 로 뽑습니다.
    }
   }
   log.info("{} | {} ", request.getMethod(), request.getRequestURI());
   log.info("{}", str);
  }
}
