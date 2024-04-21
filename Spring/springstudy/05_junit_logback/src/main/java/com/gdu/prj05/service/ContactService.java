package com.gdu.prj05.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdu.prj05.dto.ContactDto;

public interface ContactService {

  void registerContact(HttpServletRequest request, HttpServletResponse response);
  // 원래 void 가 아니죠? 이러면 컨트롤러가 서비스로부터 아무것도 받지 못함.
  // 즉 컨트롤러를 거치지 않고 바로 뷰까지 응답을 직접 response로 만들겠다는 이야기. 그래서 매개변수에 response도 껴있음!
  void modifyContact(HttpServletRequest request, HttpServletResponse response);
  void removeContact(HttpServletRequest request, HttpServletResponse response);
  List<ContactDto> getContactList();
  ContactDto getContactByNo(int contactNo);
  
  
  
  
  
}
