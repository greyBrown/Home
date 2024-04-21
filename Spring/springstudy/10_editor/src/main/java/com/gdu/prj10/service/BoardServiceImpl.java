package com.gdu.prj10.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.prj10.utils.MyFileUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

  private final MyFileUtils myfileUtils;
 
  @Override
  public ResponseEntity<Map<String, Object>> summernoteImageUpload(MultipartHttpServletRequest multipartRequest) {
    
    // 저장 경로
    String uploadPath = myfileUtils.getUploadPath();
    File dir = new File(uploadPath);
    if(!dir.exists()) {
      dir.mkdirs();
    }
    
    // 저장 파일
    MultipartFile multipartFile = multipartRequest.getFile("image");
    String filesystemName = myfileUtils.getFilesystemName(multipartFile.getOriginalFilename());
    File file = new File(dir, filesystemName);
    
    // 실제 저장
    try {
      multipartFile.transferTo(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
     
    // 반환할 Map. 뷰단으로 반환할 값은 이미지의 경로(contextPath를 포함하는 주소형식)
    // view 단으로 보낼 src = "http://localhost:8080/prj10/upload/2024/03/27/1234567890.jpg"
    // servlet-context.xml 에서 <resources> 태그를 추가한다.
    // <resources mapping="/upload/**" location="file:///upload/"> 프로젝트 외부의 일반 경로의 경우 file:// 을 붙여준다. 지금 경로는 루트아래 업로드(/upload)
    Map<String, Object> map = Map.of("src", multipartRequest.getContextPath() + uploadPath + "/" + filesystemName);
    
    
    // 반환
    return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
  }

}
