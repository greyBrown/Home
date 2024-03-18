package com.gdu.prj.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gdu.prj.dto.StudentDto;

public class DaoTest {

	 
	  private StudentDao studentDao = StudentDaoImpl.getInstance();
	  
	  @Test
	  public void 등록테스트() {
	    
	    StudentDto student = StudentDto.builder()
	                        .name("테스트")
	                        .korean(80)
	                        .english(80)
	                        .math(80)
	                      .build();
	    
	    int insertCount = studentDao.insertStudent(student);
	    
	    assertEquals(1, insertCount);
	    
	  }
	
	
}
