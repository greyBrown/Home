package com.gdu.prj02.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.prj02.dto.BlogDto;



@Controller
public class MyController4 {

  @RequestMapping("/blog/list.do")   //defualt가 GET 방식이므로 GET은 생략 -> 그렇게 value만 남는 경우, value 명시는 생략하고 value 값(경로)만 남겨줌
  public String list(Model model) {  // Request 가 아닌 스프링의 model 이라는 새로운 방식을 사용해 볼겁니다!
    
    // DB 에서 select 한 결과
    List<BlogDto> blogList = Arrays.asList(
        new BlogDto(1, "제목1"),
        new BlogDto(2, "제목2"),
        new BlogDto(3, "제목3")
        );
    
    // Model에 저장한 값은 forward 할 때 전달된다.
    model.addAttribute("blogList", blogList);         // model 에서 attribute 를 저장하는 방법 model.addAttribute
    
    
    // 기본 이동 방식은 forward 방식이다.
    
    return "blog/list";  //뷰리졸버~ views 폴더 아래의 블로그폴더의 리스트로 연결한다.
  }
  
  @RequestMapping("/blog/detail.do") 
  public String detail(@RequestParam(value="blogNo"
                                   , required=false
                                   , defaultValue="0") int blogNo, Model model) {                      // location 이동도 get 방식. 에이태그 이동과 똑같다.
    // DB 에서 가져온 데이터(실제로 DB랑 연결한건아니지만...ㅎ)
    BlogDto blog = BlogDto.builder()
                       .blogNo(blogNo)
                       .title("제목" + blogNo)
                       .build();
    
    // JSP 로 전달할 데이터
    model.addAttribute("blog", blog);
    
    // blog/detail.jsp 로 forward
    return "blog/detail";  
  }
  
  // @RequestMapping(value="/blog/add.do", method=RequestMethod.POST)
  public String add(BlogDto blog) {         //파라미터 이름과 객체 필드 이름이 동일하면 setter 를 통해 커맨드 객체로 받는다. + 자동으로 model에 저장
    
    // model을 선언하지도 addAttriute를 하지도 않았음에도 model이 알아서 가지고 간다.
    // 커맨드 객체의 Model 저장 방식 : 클래스 타입을 camelCase 로 변경해서 저장한다.(BlogDto -> blogDto 클래스로 변경해서 저장)
    
    // blog/addResult.jsp 로 forward
    return "blog/addResult";
  }
  
  @RequestMapping(value="/blog/add.do", method=RequestMethod.POST)
  public String add2(@ModelAttribute("blog") BlogDto blog) { //@ModelAttribute : 커맨드 객체가 Model에 저장되는 이름을 지정할 때 사용
    
    return "blog/addResult";            // 받을때도 request 보낼때도 request를 쓰던 것을 스프링에서 받을 때 request 보낼 땐 model로 구분을 한 것. 추가 보안작업 등...
  }                                     // 사실 model도 내부로직은 request를 활용하는 거여서 엄청 뭐 천지개벽하게 변한건아니다.
                                        // 저희는...model 씁니다!! 스프링 하잖아요!!
 
  
  
  
}
