package com.gdu.prj01.anno03;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainClass {

  public static void main(String[] args) {
    
    AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    MyController myController = ctx.getBean("myController", MyController.class);
    myController.add();
    ctx.close();
    
    
    // 이런식으로 new를 하지 않는 것이 스프링의 스타일...지금은 new 안하느라 코드가 좀 길어지는데 나중에는 아니겠져 뭐...지금 자바코드는 중요하지 않아요!!
    // getBean으로 xml에서 객체가 어떻게 작성되는지가 중요! 참고로 이렇게 xml 파일에서 setter가 타고타고 내려오는 걸 injection 이라고 부릅니당.
    // 나중에 코드가 어떻게 짧아질까? 선생님 말로는 다양한 어노테이션을 활용하게 된다고 하심...
    
    
  }

}
