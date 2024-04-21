package com.gdu.myapp;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import com.gdu.myapp.dto.UserDto;
import com.gdu.myapp.mapper.UserMapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


// Junit5
@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})

class MyAppTest {

  @Autowired
  private UserMapper userMapper;
  


  @Test
  public void 등록테스트() {
    
    UserDto user = UserDto.builder()
                        .email(null)
                        .build();
    int insertCount =  userMapper.insertUser(user);
    
    
    
    assertEquals(1, insertCount);
  }
  
  
}
