package com.gdu.myapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.myapp.service.BbsService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/bbs")
@RequiredArgsConstructor
@Controller
public class BbsController {
  
  private final BbsService bbsService;
  
  @GetMapping("/list.do")
  public String list(HttpServletRequest request, Model model) {
    bbsService.loadBbsList(request, model);
    return "bbs/list";  // model에 저장해놓은걸 포워드한다. 목록보기로 간당 고고
  }
  
  @GetMapping("/write.page")         // 여기에 인터셉터(preHandle) 걸어서 로그인여부 확인함. preHandle 의 반환값음 boolean -> 이걸로 controller의 동작을 조절함.
  public String writePage() {
    return "bbs/write";
  }
  
  @PostMapping("/register.do")
  public String register(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    //insert 이후에는 redirect 가 진행되므로 redirectAttributes 를 이용해 목록으로 성공/실패 여부를 보내준다.
    redirectAttributes.addFlashAttribute("insertBbsCount", bbsService.registerBbs(request));
    return "redirect:/bbs/list.do";
  }
  
  @PostMapping("/registerReply.do")
  public String registerReply(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    
    redirectAttributes.addFlashAttribute("insertReplyCount", bbsService.registerReply(request)); //"insertReplyCount" 이거 이름이 똑같은게 있으면 jsp에서 구분을 못함
    return "redirect:/bbs/list.do";
  }
  
  @GetMapping("/removeBbs.do")
  public String removeBbs(@RequestParam int bbsNo, RedirectAttributes redirectAttributes) {
    
    redirectAttributes.addFlashAttribute("removeBbsCount", bbsService.removeBbs(bbsNo));
    return "redirect:/bbs/list.do";
  }
  
  @GetMapping("/search.do")
  public String search(HttpServletRequest request, Model model) {
    bbsService.loadBbsSearchList(request, model);
    return "bbs/list";
  }
      
  
  
}
