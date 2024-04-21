package com.gdu.prj03.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.prj03.service.BoardService;

import lombok.RequiredArgsConstructor;

//       @Controller  @Service  @Repository
//view - controller - service - dao
@RequiredArgsConstructor
@Controller      //Controller 에서 사용하는 @Component
public class BoardController {
  
  /******************************************** DI *****************************************************/
  /* Dependency Injection
   * 1. 의존 주입
   * 2. spring Container 에 저장된 bean 을 특정 객체에 넣어 주는 것을 의미한다.
   * 3. 방법
   *    1) 필드 주입
   *    2) 생성자 주입 (보통 이걸 쓴다)
   *    3) setter 주입
   * 4. 사용 가능한 annotation
   *    1) @inject
   *    2) @Resource, @Qulifier
   *    3) @Autowired  (대부분 이걸 사용한다. 사실 다 이걸 사용한다.) 
   */
  
//  1. 필드 주입(타입을 보고 알아서 해준다 ㅎ)
//  @Autowired
//  private BoardService boardService;
  
//  2. 생성자 주입
//   1) 생성자의 매개변수로 주입된다.
//   2) @Autowired를 생략할 수 있다. (생성자주입에서만 생략가능)
//  private BoardService boardService;
//  
//  public BoardController(BoardService boardService) {
//   super();
//   this.boardService = boardService;
//  }
//  
// 3. setter 주입
//  1) 메소드의 매개변수로 주입된다.
//  2) @Autowired 를 생략할 수 없다.
//  3) 사실 메소드명은 상관이 없다.
// private BoardService boardService;
// @Autowired
//  public void setBoardService(BoardService boardService) {
//    this.boardService = boardService;
//  }
  
   // 앞으로 사용할 한 가지 방식
   // final 필드 + 생성자 주입(lombok의 @RequiredArgsConstructor를 이용해서 매개변수의 null 체크를 수행함)       >> gh 왜 static 안붙이냐? 계속 메모리에 로드되어있는 static은 성능에 문제를 일으킬 수 있다.
  
  private final BoardService boardService;
  
  

  // 타입은 인터페이스로 선언! 왠지 들을 때마다 생소해서 적어놓는다... 특히 스프링에서는 인터페이스 구현시 인터페이스를 보고 인터페이스 타입을 보고 결정해주기 때문에 인터페이스 타입~~ 
  // 스프링은 문법이 딱딱한...정형화된 프레임워크라 볼 수 있음. 하란대로 해야함
  @GetMapping("/board/list.do")
  public String list(Model model) {
    model.addAttribute("boardList", boardService.getBoardList());
    return "board/list";
  }
  
  @GetMapping("/board/detail.do")
  public String detail(int boardNo, Model model) {
    model.addAttribute("board", boardService.getBoardByNo(boardNo));
    return "board/detail";
  }
    
  
  
  

}
