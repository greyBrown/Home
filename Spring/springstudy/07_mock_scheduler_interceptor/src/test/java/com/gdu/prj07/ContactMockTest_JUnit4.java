package com.gdu.prj07;

//import static org.junit.Assert.assertNotNull;

//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;


//@RunWith(SpringJUnit4ClassRunner.class)


// ContactService 타입의 contactServiceImpl bean 이 등록된 파일
// @ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
// 컨트롤러를 root-context.xml 에 추가해줘서 문제를 해결

// WebApplicationContext 타입의 bean을 Spring Container 에 만든다.
// @WebAppConfiguration

// 테스트 수행 순서 (메소드 이름 순)

//@ContextConfiguration + @WebApplicationContext
@SpringJUnitWebConfig(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@Slf4j
public class ContactMockTest_JUnit4 {
  
  @Autowired
  private WebApplicationContext webApplicationContext;
  
  // 테스트 수행을 위한 MockMvc 객체 선언
  private MockMvc mockMvc; // 위의 @Autowired와는 상관이 없다. 이건 지금은 그냥 null값임.
  
 // MockMvc 객체 생성 (테스트 수행 이전에 생성한다)
 // @Before
  public void setUp() {
    mockMvc = MockMvcBuilders
           .webAppContextSetup(webApplicationContext)
           .build();
  }
  
//  @Test
  public void 테스트01_MockMvc생성확인() {
  //  assertNotNull(mockMvc);
  }
  
 // @Test
  public void 테스트02_삽입() throws Exception {
    MvcResult mvcResult = mockMvc.
                                perform(MockMvcRequestBuilders
                                       .post("/contact/register.do")
                                       .param("name", "테스트이름")
                                       .param("mobile", "테스트모바일")
                                       .param("email", "테스트이메일")
                                       .param("address", "테스트주소"))
                                .andReturn();
    
    
    // RedirecAttributes 에 flash attribute 를 등록하고 redirect 한 경우(복잡해져서 기록만 남김)
    // flash attribute 개념 잊지 말기!
    log.info(mvcResult.getFlashMap().toString());
    
    // HttpServletResponse 를 이용해 직접 redirect 코드를 작성한 경우
    log.info(mvcResult.getResponse().getContentAsString());
    
    
    // 원래 이거 하는 파트 -> dao!! 마이바티스가 잘 도는지 확인하려고 dao부터 controller 까지 쫙 만들어야 했던
    // 그 ㅎ ㅏㅏㅏㅏㅏ ㅜㅜㅜㅜㅜㅜ 싶은 감정을 어느정도 해소시켜주는 테스트이다.
    // 이거 마이바티스 되는지도 안되는지도 모르는데 끝까지 다 만들어요? >>> 에 대한 나름의 해결책 같다.
    // 이렇게 이해하니까 좀 수월함
  }
  
//  @Test
  public void 테스트03_상세조회() throws Exception {
    
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                             .get("/contact/detail.do")
                                             .param("contact-no", "1"))
                                 .andReturn();
    
    log.info(mvcResult.getModelAndView().getModelMap().toString());
    
  }
  


}
