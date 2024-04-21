package com.gdu.myapp.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentDto {

  private int commentNo, depth, groupNo, blogNo;
  private String contents;
  private Timestamp createDt;
  private UserDto user;
  // blogNo는 no 하나만 받아오면 될 것 같아서 Dto가 아니라 No로 받아옵니다. 항상 Dto로 받아올 필요 없어요 적절한 선에서....아니면 resultMap 이런거 다 만들어줄때 매콤해짐. 변수명도 user랑 겹쳐서 골치아파짐... 
  // 이런게 나중가면 꽤 중요한 선택이 된다...초반설계의 중요성
  
  
  
}
