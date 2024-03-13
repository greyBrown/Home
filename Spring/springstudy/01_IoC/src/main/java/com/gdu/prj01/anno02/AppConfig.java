package com.gdu.prj01.anno02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration         // 이거 안 붙이면 아무 소용없음! 어노테이션 깜빡깜빡하기 좋으니까 주의~~
public class AppConfig {
  
  @Bean
  public Student student() {
    Student student = new Student();
    student.setScores(Arrays.asList(90, 91, 92));
    student.setContacts(new HashSet<String>(Arrays.asList("02-1111-11111", "010-2222-2222")));
    student.setFriends(Map.of("동네친구", "길동", "운동친구", "또치"));
    return student;
  }
  
  

}
