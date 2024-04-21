package com.gdu.myapp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.myapp.dto.BbsDto;

@Mapper
public interface BbsMapper {
  // tmi 마커인터페이스 = 아무런 메소드 없이 텅 빈 인터페이스
  
  int insertBbs(BbsDto bbs);
  int getBbsCount();
  List<BbsDto> getBbsList(Map<String, Object> map);
  int uadateGroupOrder(BbsDto bbs);
  int insertReply(BbsDto reply);
  int removeBbs(int bbsNo);
  int getSearchCount(Map<String, Object> map);
  List<BbsDto> getSearchList(Map<String, Object> map);
  

}
