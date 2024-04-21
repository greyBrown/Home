package com.gdu.prj02.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.prj02.dto.ArticleDto;

@Controller
public class MyController3 {
  
//  /* 1. HttpServletrequest 를 이용한 요청 파라미터 처리*/
  
  @RequestMapping(value="/article/detail1.do", method=RequestMethod.GET) // <a>태그로 요청했으니까 get방식
  public String detail1(HttpServletRequest request) { // 아하 Controller에서 이렇게 메소드로 처리하니까 switch 문을 한땀한땀 뜨는 수고를 덜어준다!
     int article_no = Integer.parseInt(request.getParameter("article_no"));
     System.out.println("detail1:" + article_no);
     return "index";
    
  }
  
  /* 2. @RequestParam annotation 을 이용한 요청 파라미터 처리
   *  1) 변수를 선언하고 요청 파라미터를 받는다.
   *  2) @RequestParam annotation 은 생략할 수 있다.
   *  3) 주요 메소드
   *    (1) value        : 요청 파라미터 이름
   *    (2) required     : 요청 파라미터의 필수 여부(디폴트 true)
   *    (3) defaultValue : 요청 파라미터가 없을 때 사용할 값
   *    
   *    + 변수 두개면 RequestParam 2개 써주면 된다. 사실 순서 상관없음. 근데 누가 헷갈린다? 사람이 헷갈린다 ㅠ
   */
  
  @RequestMapping(value="/article/detail2.do", method=RequestMethod.GET)
  public String detail2(@RequestParam(value="article_no"
                                    , required = false
                                    , defaultValue = "1") int article_no) { //(int article_no)이렇게 적어주는 것만으로도 파라미터 값을 받아온다. 세상에나. 하지만 이건 많이 줄인거고 @RequestParam을 붙여준다.
    System.out.println("detail2:" + article_no);
    return "index";
    
  }
  
  
  /* 3. 커맨드 객체를 이용한 요청 파라미터 처리
   *  1) 요청 파라미터를 필드로 가진 객체를 커맨드 객체라고 한다.  (여기서는 ArticleDto도 커맨드 객체로 쓸 수 있다)
   *  2) 요청 파라미터를 setter 를 이용하여 필드에 저장한다.       (그러니까 롬복처리 안하면 또 뭐다? 어 왜 안돼)
   *  3) 자동으로 Model 에 저장된다.
   *  
   * + 커맨드 객체라는 명칭이 생소해서 그렇지..(내 생각에는) 그냥 파라미터 이름 쳐주면(필드값에 있으니까) 그거 쇽 가져오는거랑 비슷비슷한 원리인둡
   * + 참고로 주로 쓰이는 건 1번.....HttpServletRequest...아니...이러면....2번 3번의 발전은 무엇인가. 
   *   하지만 HttpServletRequest가 제공하는 파라미터 외의 장점 때문.(URI, ContextPath, IP, Session 등등등을 제공하기 때문)
   *   그렇게 쓰던데로 1번을 계속해서 쓰게된다...
   */
  @RequestMapping(value="/article/detail3.do", method=RequestMethod.GET)
  public String detail3(ArticleDto articleDto) {
    System.out.println("detail3:" + articleDto.getArticle_no());
    return "index";
  }
  
  
  
}
