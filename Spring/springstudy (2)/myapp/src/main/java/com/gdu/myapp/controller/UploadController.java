package com.gdu.myapp.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.myapp.dto.AttachDto;
import com.gdu.myapp.dto.UploadDto;
import com.gdu.myapp.service.UploadService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/upload")
@RequiredArgsConstructor
@Controller
public class UploadController {

   private final UploadService uploadService;
   
   @GetMapping("/list.do")
   public String list(HttpServletRequest request, Model model) {
     model.addAttribute("request", request);
     uploadService.loadUploadList(model);   //즉 이 model에 request가 들어있고 model이 그걸 가지고 간다.
     return "upload/list";
   }
   
   @GetMapping("/write.page")
   public String write() {
     return "upload/write";
   }
   
   @PostMapping("/register.do")
   public String register(MultipartHttpServletRequest multipartRequest
                         , RedirectAttributes redirectAttributes) {
     redirectAttributes.addFlashAttribute("inserted", uploadService.registerUpload(multipartRequest)); //count를 받아오는게 아니라 되었나 안되었나를 true/false로 받아옴
     return "redirect:/upload/list.do";
   }
   
   @GetMapping("/detail.do")
   public String detail(@RequestParam(value="uploadNo", required = false, defaultValue = "0") int uploadNo
                         ,Model model) {
     uploadService.loadUploadByNo(uploadNo, model);  // 모델은 빈 통임. 여기서 정보 담아서 가져다줘~ 서비스가 이 모델에 정보를 가져다주고, 이 모델에 담겨온 정보를 뷰단에 뿌린다.
     return "upload/detail";
   }
   
   @GetMapping("/download.do") //json 인데 produces를 쓰지 않은 이유는 service에서 이미 작성했기 때문임. 헤더에 applictation/octet-stream을 작성해줬음. 강사님 깃은 controller에는 하나 적어준 버전
   public ResponseEntity<Resource> download(HttpServletRequest request) {
     return uploadService.download(request);
   }
   
   @GetMapping("/downloadAll.do")
   public ResponseEntity<Resource> downloadAll(HttpServletRequest request) {
     return uploadService.downloadAll(request);
   }
   
   @PostMapping("/edit.do")
   public String edit(@RequestParam int uploadNo, Model model) {
     model.addAttribute("upload", uploadService.getUploadByNo(uploadNo));
     return "upload/edit";
   }
   
   @PostMapping("/modify.do")
   public String modify(UploadDto upload, RedirectAttributes redirectAttributes) {       // 오랜만에 커맨드객체타입으로!! DTO 받아와서 해봅니당
     redirectAttributes.addFlashAttribute("updateCount", uploadService.modifyUpload(upload));
     return "redirect:/upload/detail.do?uploadNo=" + upload.getUploadNo();
     // 수정하기 끝나고 목록으로 가냐 상세로 가냐 정해주면 된다. "redirect:/list.do"; "redirect:/detail.do?uploadNo=" + upload.getUploadNo(); 이런식으로. 근데 이건 지금 안됨.
     // 디테일은 어느 넘버 게시글로 갈지 매개변수로 넘겨줘야 하니까 그런거 주의. 이 경우 uploadNo가 아예 edit.jsp에서 안넘어온다(넘어오는거 3개뿐임)
     // uploadNo 따로 담아서 보내줘야 한다. hidden으로 uploadNo 담는 input 하나 만들어준다. 이제 됨 ~.~
   }
   
   @GetMapping(value="/attachList.do", produces="application/json")
   public ResponseEntity<Map<String, Object>> attachList(@RequestParam int uploadNo) {
     return uploadService.getAttachList(uploadNo);
   }
   
   @PostMapping(value="/removeAttach.do", produces="application/json")
   public ResponseEntity<Map<String, Object>> removeAttach(@RequestBody AttachDto attach){ //map 으로 받건 AttachDto 로 받건 둘중 하나로(map 혹은 객체로 requestbody 받는다)
     return uploadService.removeAttach(attach.getAttachNo());
   }
   
   
}
