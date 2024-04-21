package com.gdu.myapp.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.myapp.dto.BlogDto;

public interface BlogService {

  // 이미지 첨부 누르면 게시글에 이미지가 똭 뜨는거(SPA 니까 AJAX 으로 동작)구현
  ResponseEntity<Map<String, Object>> summernoteImageUpload(MultipartFile multipartFile);  //multipart(이미지 하나당 한번씩 호출)는 이미 해봤으니 딴걸로...MultipartFile로 함.
  int registerBlog(HttpServletRequest request);  //  복습 : 파라미터로 Request, RequestParam(변수가 많아지면 별로다), CommandObj 셋 중 하나로 받을 수 있다. 하지만 이번에도 request 로...^^!
  ResponseEntity<Map<String, Object>> getBlogList(HttpServletRequest request);
  // 만약 목록을 10개만 가져가자 ~ 하면 List도 가능한 선택이겠지만 +alpha로 가져갈 것 이기 때문에 Map 으로 간다 
  int updateHit(int blogNo);
  BlogDto getBlogByNo(int blogNo);
  int registerComment(HttpServletRequest request);
  Map<String, Object> getCommentList(HttpServletRequest request);
  int registerReply(HttpServletRequest request);
  int removeComment(int commentNo);
  int modifyBlog(int blogNo);
  
}
