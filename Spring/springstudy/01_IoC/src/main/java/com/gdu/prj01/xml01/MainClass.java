package com.gdu.prj01.xml01;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainClass {
  
  
  public static void method01() {
    
    // appCtx.xml 읽기
    AbstractApplicationContext ctx = new GenericXmlApplicationContext("com/gdu/prj01/xml01/appCtx.xml");
    
    // appCtx.xml 에 등록한 빈(bean) 가져오기
    Calculator calculator = ctx.getBean("calculator", Calculator.class);
    
    // 가져온 빈(bean) 사용하기
    calculator.add(10, 20);
    calculator.sub(10, 5);
    calculator.mul(10, 3);
    calculator.div(10, 4);
    
    // appCtx.xml 닫기
    ctx.close();
    
    // 어째 코드가 더 복잡해진것 같죠?! 하지만 점점 간단해지고...지금 이건 외울필요는 없는 코드예요. 크게 중요하지도 않구요~
    //그냥..제어의 역전란게... 객체를 개발자 대신 Spring이 생성(new)한다.... 외부 프레임워크(Spring)이 제어 흐름을 대신한다!
    // 대신 xml 에서 만든 <bean>태그 이거는 공부해야함!! 빈을 만드는 방법을 알아야함.
   
    
  }
  
  public static void method02() {
    
    AbstractApplicationContext ctx = new GenericXmlApplicationContext("com/gdu/prj01/xml01/appCtx.xml");
    
    Computer computer1 = ctx.getBean("computer1", Computer.class);
    
    System.out.println(computer1.getModel());
    System.out.println(computer1.getPrice());
    computer1.getCalculator().add(5, 2);   
    computer1.getCalculator().sub(5, 2);   
    computer1.getCalculator().mul(5, 2);   
    computer1.getCalculator().div(5, 2);
    
    ctx.close();
  }
  
  public static void method03() {
    
    AbstractApplicationContext ctx = new GenericXmlApplicationContext("com/gdu/prj01/xml01/appCtx.xml");
    Computer computer2 = ctx.getBean("computer2", Computer.class);
    System.out.println(computer2.getModel());
    System.out.println(computer2.getPrice());
    computer2.getCalculator().add(5, 2);
    computer2.getCalculator().sub(5, 2);
    computer2.getCalculator().mul(5, 2);
    computer2.getCalculator().div(5, 2);
    
    ctx.close();
    
  }

  public static void main(String[] args) {
      
    method03();

    
  }

}
