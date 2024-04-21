package com.gdu.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AttachDto {

  private int attachNo, downloadCount, hasThumbnail, uploadNo;
  private String uploadPath, filesystemName, originalFilename;
  // 업로드 정보에 대한 상세보기가 있을 것이고, 첨부된 파일들의 목록이 쫙 나올 것임.
  // 언제 첨부되었는지는 상세보기에서 표시해 줄 수 있으니, 언제 첨부되었는지를 Dto에 포함시킬 필요는 없음
  // 그러니까 UploadDto upload; 를 여기서 필드로 줄 필요는 없다는 뜻. 있으면 괜히 resultMap도 해줘야 하고....굳이 이래야할 메리트가 없음
  // 그러니까 그냥 uploadNo 라는 int 값으로 포함시켜 줌
  
}
