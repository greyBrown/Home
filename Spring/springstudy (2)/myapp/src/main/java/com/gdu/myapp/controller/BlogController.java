package com.gdu.myapp.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.myapp.service.BlogService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/blog")
@Controller
@RequiredArgsConstructor
public class BlogController {
  
  private final BlogService blogService;
  
  @GetMapping("/list.page")
  public String list() {
    return "blog/list";
    // 평소에는 여기서 service를 불렀지만...스크롤에서는 그냥 단순 페이징 이동("/list.page")만 함. header랑 write 도 do로 해놨었으니까 바꿔줌.
    //왜? ajax으로 가져올 것이기 때문에. 페이지 이동 후 ajax이 한번 돌면서 최초 목록을 뿌려줌. 휠을 내리면 그다음 목록 뿌리고...이런식으로
  }
  
  @GetMapping("/write.page")
  public String writePage() {
    return "blog/write";
  }
  
  @PostMapping(value="/summernote/imageUpload.do", produces = "application/json")
  public ResponseEntity<Map<String, Object>> summernoteImageUpload(@RequestParam("image") MultipartFile multipartFile){
    return blogService.summernoteImageUpload(multipartFile);
  }
  
  @PostMapping("/register.do")
  public String register(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("insertCount", blogService.registerBlog(request));
    return "redirect:/blog/list.page";
  }
  
  @GetMapping(value="/getBlogList.do", produces="application/json")
  public ResponseEntity<Map<String, Object>> getBlogList(HttpServletRequest request){
    return blogService.getBlogList(request);
  }
  
  @GetMapping("/updateHit.do")
  public String updateHit(@RequestParam int blogNo) {
    blogService.updateHit(blogNo);
    return "redirect:/blog/detail.do?blogNo=" + blogNo;  // detail 은 blogNo 을 매개변수로 넘겨줘야 한다!! 이런거 잘 해줘야 한다 안그러면 뭐야 왜 안되는데 뭔데 사태가 벌어진다
  }
  
  
  @GetMapping("/detail.do")
  public String detail(@RequestParam int blogNo, Model model) {
    model.addAttribute("blog", blogService.getBlogByNo(blogNo));
    return "blog/detail";
  }
  
  @PostMapping(value="/registerComment.do", produces = "application/json")
  public ResponseEntity<Map<String, Object>> registerComment(HttpServletRequest request){
   
    
    //return new ResponseEntity<Map<String,Object>>(Map.of("insertCount", blogService.registerComment(request))
    //                                                     , HttpStatus.OK);
    
    // 좀 더 간략한 버전을 이제 사용해 봅시다! 마지막 OK를 따로 추가할 필요없이 바로 추가해준 메소드
    return ResponseEntity.ok(Map.of("insertCount", blogService.registerComment(request)));
  }
  
  @GetMapping(value="/comment/list.do", produces = "application/json")
  public ResponseEntity<Map<String, Object>> commentList(HttpServletRequest request){
    return new ResponseEntity<>(blogService.getCommentList(request), HttpStatus.OK);
  }
  
  
  @PostMapping(value="/comment/registerReply.do", produces = "application/json")
  public  ResponseEntity<Map<String, Object>> registerReply(HttpServletRequest request){
    return ResponseEntity.ok(Map.of("insertReplyCount", blogService.registerReply(request)));
  }
  
  @GetMapping(value="/comment/removeComment.do")
  public String removeComment(@RequestParam int commentNo, int blogNo) {
    blogService.removeComment(commentNo);
    return "redirect:/blog/detail.do?blogNo=" + blogNo;
  }
  
  //이러면 안된다는걸 깨달음 ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ blogNo 가지고 modify.page 가는 맵핑이 하나있고, 거기서 modify.do 하는 맵핑이 있어야함. 
  @GetMapping(value="/modify.page")
  public String modifyComment(@RequestParam int blogNo, Model model) {
    model.addAttribute("blog", blogService.getBlogByNo(blogNo));
    return "blog/modify";
   // return "redirect:/blog/detail.do?blogNo=" + request.getParameter("blogNo");
  }
  

  
  
  
}
