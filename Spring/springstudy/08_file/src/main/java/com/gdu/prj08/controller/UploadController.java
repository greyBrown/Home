package com.gdu.prj08.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.prj08.service.UploadService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UploadController {

  private final UploadService uploadService;
  
  
  @PostMapping("/upload1.do")
  public String upload1(MultipartHttpServletRequest multipartHttpServletRequest
                      , RedirectAttributes redirectAttributes) { // redirect도 요청을 가지고 갈 수 있게 지원. 여기서 테스트해봐요~
    //파일 업로드에서 HttpRequest는 못씀. request: 파일이요? 제가요? -> MultipartHttpServletRequest 라는 클래스가 담당한다.
    
    
    int insertCount = uploadService.upload1(multipartHttpServletRequest);
    redirectAttributes.addFlashAttribute("insertCount", insertCount);
    return "redirect:/main.do";  //index로 돌아가는 주소 2개 (/, /main.do). forward에서 이렇게 쓰면 뷰리졸버때문에 이상한대로 가는것 알려드립니다.. 그리고 지금은 아니지만 사실상 업로드가 insert라서 redirect 해야해용
  }
  
 @PostMapping(value="/upload2.do", produces="application/json")
 @ResponseBody   //ajax 처리할 때 꼭 필요함!     
  public Map<String, Object> upload2(MultipartHttpServletRequest multipartHttpServletRequest){
   return uploadService.upload2(multipartHttpServletRequest); //jackson이 map을 자동으로 json 데이터로 바꿔준다(그 반대도 수행)
   // 갈 수록 많이 쓰이는 방법. 목록을 넘길때도 이런방식으로 넘기게 되기 때문에...
 }
 
 /*
 @PostMapping(value="/upload2.do", produces="application/json")
  public ResponseEntity<Map<String, Object>> upload2(MultipartHttpServletRequest multipartHttpServletRequest){
   return new ResponseEntity(Map.of("success", 1), HttpStatus.OK); return부분은 서비스가 이렇게 구성해서 보내줌 
   //ResponseEntity를 통해 수행하는 방법. ResponseBody가 필요없음! 두가지 방법 모두 알고 있어야 함.
 }
 */
  
}
