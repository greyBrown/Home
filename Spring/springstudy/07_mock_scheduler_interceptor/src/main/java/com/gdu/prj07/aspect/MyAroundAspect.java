package com.gdu.prj07.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Aspect
// @Component 이친구를 빼고 appConfig에 직접 빈으로 만들어 볼 거입니다. aspect를 하나만 할거면 component로 하는데 하나가 아니니까 따로 만듭니다!
public class MyAroundAspect {

  // PointCut : 언제 동작하는가?
  @Pointcut("execution (* com.gdu.prj07.controller.*Controller.*(..))") // 표현식 써줌.
  // 지금 컨트롤러 패키지에는 컨트롤러 밖에 없어서 *Controller가 아니라 * 만 써도 되긴합니다. 하지만 지금은 확실히 씀.
  //(..) >> 모든 매개변수. 하지만 (*)이거 쓰는 경우는 없다. 보통 다 (..)이걸로 커버함

   public void setPointCut() {}
  
  
  
  
  // Advice   : 무슨 동작을 하는가?
  @Around("setPointCut()")
  // 이렇게 어노테이션에 연결해준다. 이제 만들어줄 이 around 가 setpointcut 시점에 동작함
  
  /*
   * Around Advice 메소드 작성 방법
   * 1. 반환타입  : Object
   * 2. 메소드명  : 마음대로
   * 3. 매개변수  : ProceedingJoinPoint 타입 객체
   * 
   * 이렇게 형식이 정해져있다. 약속되어있는 사항들임
   */
  
  public Object myAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { //exception 넣어보고 안되면 throwable. 더 상위임.
    // 시점을 지정해줘야 한다. 전에 동작할 코드, 후에 동작할 코드를 지정해줘야함. before after는 시점이 정해졌으니 반환타입이 딱히 필요없다. 
    // 프로시드 전에 작성해주면 이전에 동작하는 코드, 프로시드 이후에 작성해주면 이후에 동작하는 코드.

    log.info("{}", "-".repeat(80));                                                   // 동작 이전(@Before 이전)
    
    Object obj = proceedingJoinPoint.proceed();                                       // 동작 시점
    
    log.info("{}\n", "-".repeat(80));                                                  // 동작 이후 (@After 이후)
    
    // + 출력할 내용 앞에 "{}" 이렇게 넣어주면 알아서 이 사이에 끼고 나온다. 옵션인듯.
    
    
    
    return obj;
  }
  
  
  
  
  
}
