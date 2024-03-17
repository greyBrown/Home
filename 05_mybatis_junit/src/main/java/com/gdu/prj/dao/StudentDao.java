package com.gdu.prj.dao;

import java.util.List;
import java.util.Map;

import com.gdu.prj.dto.StudentDto;


public interface StudentDao {

	  int insertBoard(StudentDto board);
	  int updateBoard(StudentDto board);
	  List<StudentDto> selectAll(Map<String, Object> params);
	  List<StudentDto> selectByScore(int begin, int end);
	  void close();
	
	
}
