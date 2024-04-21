package com.gdu.prj04.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.prj04.dto.BoardDto;
import com.gdu.prj04.service.BoardService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/ajax1")  // /ajax1 로 시작하는 모든 요청을 담당하는 컨트롤러
@Controller
@RequiredArgsConstructor
public class BoardController1 {

  private final BoardService boardService;
  
  
  @ResponseBody  // 반환 값은 jsp 의 이름이 아니고 어떤 데이터이다. (비동기 작업에서 꼭 필요한 annotation)
  @GetMapping(value="/list.do", produces="application/json") // produces : 응답 데이터 타입 (Content-Type)  자바에서 쓰는 데이터의 컨텐트타입 명시
  public List<BoardDto> list() { // jackson 라이브러리가 List<BoardDto>를 JSON 데이터로 변환한다.
    return boardService.getBoardList();  // 뷰리졸버가 여기다 알아서 .jsp를 붙이겠죠? 그러지 못하게 처리를 해줘야함. 
  }                                       //이게 jsp가 아니다! 데이터다! -> @ResponseBody 처리
  
  
  @ResponseBody
  @GetMapping(value="/detail.do", produces="application/json")
  public BoardDto detail(int boardNo) {      //requestParam을 생략
      return boardService.getBoardByNo(boardNo); 
  }
  
  
  
}
