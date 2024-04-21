package com.gdu.prj05.dao;

import java.util.List;

import com.gdu.prj05.dto.ContactDto;

public interface ContactDao {

  int registerContact(ContactDto contact);  // 이 메소드명이 멥퍼의 태그 아이디가 됨. rgrg 파라미터 타입은 풀경로로 작성
  int modifyContact(ContactDto contact);
  int removeContact(int contactNo);
  List<ContactDto> getContactList();
  ContactDto getContactByNo(int contactNo);
}
