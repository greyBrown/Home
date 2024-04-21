package com.gdu.prj04.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gdu.prj04.dto.BoardDto;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor // DI 에서 배운 처리~ 아래 board1 2 3 은 이제 더 이상 null 값이 아님. 생성자에 의해서 appconfig에 있는 객체들과 연결. 연습겸 이렇게 한거고 appconfig 자체를 잘 안씀. 다음 방법은 @repository로 할게용
public class BoardDaoImpl implements BoardDao {
  


  private BoardDto board1;
  private BoardDto board2;
  private BoardDto board3;
  
  @Autowired
 
  
  @Override
  public List<BoardDto> getBoardList() {
 
    return Arrays.asList(board1, board2, board3);
  }

  @Override
  public BoardDto getBoardByNo(int boardNo) {
    BoardDto board = null;
    switch(boardNo) {
    case 1 : board = board1; break;
    case 2 : board = board2; break;
    case 3 : board = board3; break;
    }
    return board;
  }

}
