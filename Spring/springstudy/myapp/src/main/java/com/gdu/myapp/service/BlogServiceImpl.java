package com.gdu.myapp.service;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.myapp.mapper.BlogMapper;
import com.gdu.myapp.utils.MyFileUtils;
import com.gdu.myapp.utils.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

  private final BlogMapper blogMapper;
  private final MyPageUtils myPageUtils;
  private final MyFileUtils myFileUtils;
  private ServletContext servlet;
  
  
  @Override
  public ResponseEntity<Map<String, Object>> summernoteImageUpload(MultipartFile multipartFile) {
    
    // 이미지 저장할 경로 생성
    String uploadPath = myFileUtils.getUploadPath();
    File dir = new File(uploadPath);
    if(!dir.exists()) {
      dir.mkdirs();
    }
    
    // 저장할 이름 생성
    String filesystemName = myFileUtils.getFilesystemName(multipartFile.getOriginalFilename());
    
    
    // 이미지 저장
    File file = new File(dir, filesystemName);
    try {
      multipartFile.transferTo(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
     
    
   
    Map<String, Object> map = Map.of("src", uploadPath + "/" + filesystemName);
    
    // contextPath 문제 때문에 그냥 다시 request 하나 넣어줬지만...잘 돌아가는데 괜히 오류 날 것 같아서 servlet에서 뺀 거 쓰겠음.
    // 나중에 문제생기면 강사님 깃 보고 contextPath request로 뽑아서 넣기...ㅎㅎ.....
    // 원래 웹 관련한거(서블릿같은거)는 컨트롤러에서 처리하고 매개변수로 받아오는게 맞는 방법임! 그게 더 안정성이 높고 뭐시기뭐시기....의존성과 유지보수성....
    // 하지만 지금은 그거 고치다가 잘 돌아가는거 변경하기 싫죠?????????? + 강사님이 마지막에 찾아내신 것. "그냥 jsp 에서도 붙였어도 됐네!!" -> 일케 변경함 ㅋㅋ
    
    // 미리보기를 위해 필요한게 있죠?? <resources mapping="/upload/**" location="file:///upload/"/> 이거!! 이거이거 다시 찾아보기
    // 가물가물하면 가서 한번 설명 더 보기 ...ㅜ
    
    // 반환
    return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
  }

  @Override
  public int registerBlog(HttpServletRequest request) {
    // TODO Auto-generated method stub
    return 0;
  }

}
