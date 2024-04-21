package com.gdu.prj09.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressDto {
  private int addressNo;
  private String zonecode;
  private String address;
  private String detailAddress;
  private String extraAddress;
  private MemberDto member;
  // 쿼리문에서 select 해온 결과가 MemberDto member 안에 들어갈 수 있다는 것을 mapper에서 명시(작성)해줘야 한다.
  // 바로 저장이 안되서 하는 작업...되면 좋겠지만 안된다! 명시해줘야 한다.
  
  /*AddressDto
   * zone
   * addr
   * detail
   * extra
   * MemberDto(memberNo,email, name, gender)
   * 이렇게 집어 넣을거임
   * 
   * int memberNo를 들어내고 그 자리에 MemberDto를 넣음 -> 한 Dto에 json에서 넘겨주는 데이트를 받을 필드가 모두 존재하게 됨
   */   
}
