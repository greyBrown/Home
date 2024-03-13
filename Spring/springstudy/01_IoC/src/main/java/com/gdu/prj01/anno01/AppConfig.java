package com.gdu.prj01.anno01;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  //IoC Container 에 bean 을 등록하는 클래스 -> xml 에서 열심히 등록했던걸 java 에서 해보겠다
public class AppConfig {
  
  /*
   * 반환타입 : bean 의 타입, <bean class="">
   * 메소드명 : bean 의 이름, <bean id="">  
   * 
   * @Bean(name="") 이렇게 해주면 메소드명은 상관없어지지만...이건 이론상 가능은 하다 정도고 실제로 그닥 쓰이지 않음. 
   * // GH 어 id 인데 name으로 하네? -> 약간의 차이가 있지만 둘이 혼용할 수 있습니다. id가 좀 더 본질적임 중복체크도 해주고.... 구선생님이 그러셨음.
   *  
   */
  
  
  @Bean // 이거 안해주면 노소용!! 롬복없이 xml 작성하는 꼴 되는 거임....
  public Calculator calculator() {
    return new Calculator();
  }
  
  @Bean
  public Computer computer1() {
    Computer computer1 = new Computer();
    computer1.setModel("gram");
    computer1.setPrice(200);
    computer1.setCalculator(calculator());
    return computer1;
  }
  
  @Bean
  public Computer computer2() {
    return new Computer("macbook", 300, calculator());
  }

  
  
  
}
