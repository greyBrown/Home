package com.gdu.prj04.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.prj04.dto.BoardDto;
import com.gdu.prj04.service.BoardService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/ajax3")
@RequiredArgsConstructor
@Controller  //@ResponseBody annotation 은 ResponseEntity 클래스가 제공한다.
public class BoardController3 {
  
  private final BoardService boardService;
  
  
  @GetMapping("/list.do") //produces 랑은 다른 방법으로! 기능하는 건 똑같으니까 아 넘 어려운데; 싶으면 걍 produces 로...ㅋㅋㅋ
  public ResponseEntity<List<BoardDto>> list() {
    
    // 응답 헤더
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/json");            // 그렇군...Header...내가 이렇게 달아 주기도 하는 거였군...
    
    // 반환
    return new ResponseEntity<List<BoardDto>>(boardService.getBoardList()                 // 데이터
                                            , header                                      // 응답헤더
                                            , HttpStatus.OK);                             // 응답코드
  }
  
  @GetMapping("/detail.do")
  public ResponseEntity<BoardDto> detail(int boardNo){  //requestparam 생략
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/json");          // 요 데이터 종류를 바꾸면 다른 데이터를 받는 것도 가넝합니다! 여러가지 받을 수 있어요.
    
    return new ResponseEntity<BoardDto>(boardService.getBoardByNo(boardNo), header, HttpStatus.OK);
  }
  
  
  
  
  
}
