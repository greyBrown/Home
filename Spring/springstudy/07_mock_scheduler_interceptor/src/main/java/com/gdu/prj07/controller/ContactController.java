package com.gdu.prj07.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.prj07.service.ContactService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/contact")
@RequiredArgsConstructor
public class ContactController {

  private final ContactService contactService;
  
    
    @GetMapping(value="/list.do")
    public ModelAndView list() {
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.addObject("contactList", contactService.getContactList());
      modelAndView.setViewName("contact/list");
      return modelAndView;     // ModelAndView...옛적엔 이렇게 썻었다...그래서 이런 클래스가 남아있따~~
    }
    
    @GetMapping(value="/detail.do")
    public String detail(@RequestParam(value="contact-no", required=false, defaultValue="0")  int contactNo
                       , Model model) {
      model.addAttribute("contact", contactService.getContactByNo(contactNo));
      return "contact/detail";
    }
  
    @GetMapping(value="/write.do")
    public String write() {
      return "contact/write";
    }
    
    @PostMapping(value="/register.do") 
    public void register(HttpServletRequest request, 
                       HttpServletResponse response) {
      contactService.registerContact(request, response);
    }
    
    @GetMapping(value="/remove.do")
    public void remove1(HttpServletRequest request, HttpServletResponse response) {
      contactService.removeContact(request, response);
    }
    
    @PostMapping(value="/remove.do")
    public void remove2(HttpServletRequest request, HttpServletResponse response) {
      contactService.removeContact(request, response);
    }
    
    @PostMapping(value="/modify.do")
    public void modify(HttpServletRequest request, HttpServletResponse response) {
      contactService.modifyContact(request, response);
    }
    
//    @GetMapping(value="/tx/test.do")
//    public String txTest() {
//      contactService.txTest();
//      return "redirect:/contact/list.do";
//    } index 에서 index 링크 지우고 테스트가 끝났으니까 주석처리
    
  
}
