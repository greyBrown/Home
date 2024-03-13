package com.gdu.prj01.anno01;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainClass {
  
  
  public static void method01() {
    

    AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);  //이제 xml 이 아님!!
        
    Calculator calculator = ctx.getBean("calculator", Calculator.class);

    calculator.add(10, 20);
    calculator.sub(10, 5);
    calculator.mul(10, 3);
    calculator.div(10, 4);
    
    ctx.close();
    
    
  }
  
  public static void method02() {
    
    AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    
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
    
    AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
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
