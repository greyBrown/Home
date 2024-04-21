package com.gdu.prj07.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;

@Slf4j    // 로그 찍으려고 달아주는 거임
@Aspect
public class MyAfterAspect {

  // PointCut
  @Pointcut("execution(* com.gdu.prj07.controller.*Controller.*(..))")
  public void setPointCut() {}
  
 
  // Advice
  /*
   * After Advice 메소드 작성 방법
   * 1. 반환 타입 : void
   * 2. 메소드명  : 마음대로
   * 3. 매개변수  : JoinPoint 타입 객체
   */  
  @After("setPointCut()")
  public void myAfterAdvice(JoinPoint joinPoint) {
    
    log.info("{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date()));
    
    // aop를 통해 공통로그 - uri/parameter/method/- 시간을 출력하는 로그 작성
    
  }
  
}
