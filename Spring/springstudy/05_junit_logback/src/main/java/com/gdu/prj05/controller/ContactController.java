package com.gdu.prj05.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.prj05.service.ContactService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/contact")
@RequiredArgsConstructor
public class ContactController {

  private final ContactService contactService;
  
  // private static final Logger log = LoggerFactory.getLogger(ContactController.class);  // ContactController 가 동작할 때 로그를 찍는 log
  // --> 요 선언 lombok 의 @Slf4j 어노테이션이 대신 해준다! 끗~~
  
  // 메소드마다 로그를 넣어주고, 로그가 request가 필요하니까 메소드에 request 없으면 그것도 넣어주는 작업도 한다.
  // 그렇다. 어떻게 프로젝트마다 이 로그작업만 하루종일 할 것인가? 그걸 내일 배울 "AOP"를 통해서 보다 간편하게 작업해 봅니다.
    
    @GetMapping(value="/list.do")
    public String list(HttpServletRequest request, Model model) { //로그 때문에 request를 붙여넣어줌
      log.info(request.getMethod()+ " / " + request.getRequestURI());  // 메소드와 요청주소를 로그로 남김
      model.addAttribute("contactList", contactService.getContactList());
      return "contact/list";      // view Resolver
    }
    
    @GetMapping(value="/detail.do")
    public String detail(HttpServletRequest request, @RequestParam(value="contact-no", required=false, defaultValue="0")  int contactNo
                       , Model model) {
      // 만약 파라미터가 비었으면 0이라고 처리하겠다. (DB 상에 0 이 없으니 상세보기 못하고 끝)
      log.info(request.getMethod()+ " / " + request.getRequestURI());
      model.addAttribute("contact", contactService.getContactByNo(contactNo));
      return "contact/detail";
    }
  
    @GetMapping(value="/write.do")
    public String write(HttpServletRequest request) {
      log.info(request.getMethod()+ " / " + request.getRequestURI());
      return "contact/write";
    }
    
    @PostMapping(value="/register.do") 
    public void register(HttpServletRequest request, HttpServletResponse response) {
      // 어제 모든 작업은 서비스에서 처리하는 걸로 작업했음. 그래서 service는 void return임. 컨트롤러가 받는게 없다.
      // request 랑 response는 서블릿만 선언할 수 있으니 그것만 선언해서 서비스로 넘겨주면 된다.
      log.info(request.getMethod()+ " / " + request.getRequestURI());
      contactService.registerContact(request, response);
    }
    
    @GetMapping(value="/remove.do")
    public void remove1(HttpServletRequest request, HttpServletResponse response) {
      log.info(request.getMethod()+ " / " + request.getRequestURI());
      contactService.removeContact(request, response);
    }
    
    // Get 으로 들어오는 remove 와 Post 로 들어오는 remove 모두 처리하기 위해 이렇게 맵핑@ 을 다르게 달아준다.
    // 문제없이 작동함. 나중에 같은 주소로 요청 들어오는 거 많아질때 자주 쓰게 된다.
    @PostMapping(value="/remove.do")
    public void remove2(HttpServletRequest request, HttpServletResponse response) {
      log.info(request.getMethod()+ " / " + request.getRequestURI());
      contactService.removeContact(request, response);
    }
    
    @PostMapping(value="/modify.do")
    public void modify(HttpServletRequest request, HttpServletResponse response) {
      log.info(request.getMethod()+ " / " + request.getRequestURI());
      contactService.modifyContact(request, response);
    }
    
   
    
  
}
