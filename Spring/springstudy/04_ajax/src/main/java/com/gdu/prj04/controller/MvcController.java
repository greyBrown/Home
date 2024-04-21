package com.gdu.prj04.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.prj04.dao.BoardDao;

@Controller
public class MvcController {

  @Inject
  private BoardDao boardDao;
  
 @GetMapping(value = {"/", "/main.do"})
  public String welcome() {
   return "index";
  }
 
 
 @GetMapping("exercise1.do")        
 public void exercise1() { }   // void 로 해서 return 생략하는 방법으로

 @GetMapping("exercise2.do")  // 1번이랑 다르게 String return 하는 방법으로
 public String exercise2() {  
   
   return "exercise2";
 }
 
 @GetMapping("exercise3.do")
 public String exercise3() {
   return "exercise3";
 }
 
 
 
}
