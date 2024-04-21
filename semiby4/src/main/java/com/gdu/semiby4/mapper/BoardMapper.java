package com.gdu.semiby4.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.semiby4.dto.AttachDto;
import com.gdu.semiby4.dto.BoardDto;
import com.gdu.semiby4.dto.CommentDto;

@Mapper
public interface BoardMapper {
  
  int getBoardCount();
  List<BoardDto> getBoardList(Map<String, Object> map);
  BoardDto getBoardByNo(int boardNo);
  List<AttachDto> getAttachList(int uploadNo);
	int insertBoard(BoardDto board);
  int insertAttach(AttachDto attach);
	int insertComment(CommentDto comment);
  int getCommentCount(int boardNo);
  List<CommentDto> getCommentList(Map<String, Object> map);
	int getSearchCount(Map<String, Object> map);
  List<BoardDto> getSearchList(Map<String, Object> map);
  
 // 순지선이 다운로드를 위해 추가한 맵퍼
  AttachDto getAttachByNo(int attachNo);
  int updateDownloadCount(int attachNo);

 // 집에서 추가
  List<BoardDto> getBoardMultiList(Map<String, Object> map);
  List<BoardDto> getBoardDetailList(Map<String, Object> map);
  
  
}
