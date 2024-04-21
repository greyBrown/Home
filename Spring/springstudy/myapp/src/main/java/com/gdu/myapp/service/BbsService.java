package com.gdu.myapp.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface BbsService {
  int registerBbs(HttpServletRequest request);
  void loadBbsList(HttpServletRequest request, Model model);                                        //반환 안하고 model에 잔뜩 실어만 줄 것임
  // 매개변수 request로 쫙 통일하고, model이 필요하면 request에 model을 넣고 쓸 때 request에서 model을 빼서 쓰기도 함. 이런 스타일도 있다~ 그렇다는 것
  // 정 반대로 model(자바의 Map이 Model의 베이스임. Map과 유사...그래서 service에서 전부 꺼내쓸 수 있다.)로 통일해서 그 안에 attribute로 request를 넣기도 하고...그렇다.
  int registerReply(HttpServletRequest request);          // 엄밀히말하자면 댓글(Comment)가 아니라 답글(Reply). 요컨대 로직이 다르다.
  int removeBbs(int bbsNo);
  void loadBbsSearchList(HttpServletRequest request, Model model);   
}
