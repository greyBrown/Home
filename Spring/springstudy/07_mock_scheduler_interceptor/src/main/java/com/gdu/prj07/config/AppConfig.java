package com.gdu.prj07.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.gdu.prj07.aspect.MyAfterAspect;
import com.gdu.prj07.aspect.MyAroundAspect;
import com.gdu.prj07.aspect.MyBeforeAspect;

@EnableAspectJAutoProxy  //Aspect 허용 어노테이션
@Configuration
public class AppConfig {

  @Bean
  public MyAroundAspect myAroundAspect() {
    return new MyAroundAspect();
  }   // 아까의 myAroundAspect의 @component를 대체하는 코드
  
  //동작은 잘 되는데...{} 사이에 무슨 controller 라고 나오는데... 무늬가 안뜬다
  // 무늬는 나중에 뜨기도 한다고 하심...어 떴다!!!!
  
  @Bean
  public MyBeforeAspect myBeforeAspect() {
    return new MyBeforeAspect(); // 화살표 무늬가 어라운드랑 다름. 윗부분만~
  }
  
  @Bean
  public MyAfterAspect myAfterAspect() {
    return new MyAfterAspect();
  }
  
}
