package com.gdu.prj10.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MyFileUtils {

  // 현재 날짜
  public static final LocalDate TODAY = LocalDate.now();
  
  
  // 업로드 경로 반환
  public String getUploadPath() {
    return "/upload" + DateTimeFormatter.ofPattern("/yyyy/MM/dd").format(TODAY);
  }
  
  // 저장될 파일명 반환(원래 파일명이 아니라 저장될 파일명)
  public String getFilesystemName(String originalFilename) {
    String extName = null;           // 확장자를 신경써줘야 한다. 파일명.확장자
    if(originalFilename.endsWith(".tar.gz")){ // 확장자에 마침표가 포함된 경우 
      extName = ".tar.gz";
    } else {
      extName = originalFilename.substring(originalFilename.lastIndexOf(".")); //마지막 .(마침표) 에서부터 끝까지
    }
    return UUID.randomUUID().toString().replace("-", "") + extName;       //UUID가 이런 이름 만들때 여러모로 짧고 보안성 높고 좋음. 근데 하이픈이 쭈루룩 생기니까 지워준다.                                                            
  }
  
  
  
  
  
  
  
  
  
}
