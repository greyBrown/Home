package com.gdu.myapp.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdu.myapp.service.UploadService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RemoveTempFilesScheduler {
  
  private final UploadService uploadService;
  
  // 12시 20분에 removeTempFiles 서비스가 동작 하는 스케쥴러 
  
  @Scheduled(cron = "0 20 12 * * ?")
  public void execute() {
    
    uploadService.removeTempFiles();
    System.out.println("12:20 분 해당 폴더 내의 파일들이 모두 삭제되었습니다.");
  }

}
