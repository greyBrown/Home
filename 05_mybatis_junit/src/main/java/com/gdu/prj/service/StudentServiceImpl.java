package com.gdu.prj.service;

import com.gdu.prj.common.ActionForward;
import com.gdu.prj.dao.StudentDao;
import com.gdu.prj.dao.StudentDaoImpl;
import com.gdu.prj.dto.StudentDto;

import jakarta.servlet.http.HttpServletRequest;

	

public class StudentServiceImpl implements StudentService {
	
	private StudentDao studentDao = StudentDaoImpl.getInstance();

	@Override
	public ActionForward addStudent(HttpServletRequest request) {
		String name = request.getParameter("name");
		int korean = Integer.parseInt(request.getParameter("korean"));
		int english = Integer.parseInt(request.getParameter("english"));
		int math = Integer.parseInt(request.getParameter("math"));
		StudentDto student = StudentDto.builder()
				               .name(name)
				               .korean(korean)
				               .english(english)
				               .math(math)
				               .build();
		int insertCount = studentDao.insertStudent(student);
		String view = null;
	    if(insertCount == 1) {
	      view = request.getContextPath() + "/student/list.do"; 
	    } else if(insertCount ==0) {
	      view = request.getContextPath() + "/student/list.do";
	    }
	    //INSERT 이후 이동은 redirect 이다.
	    return new ActionForward(view, true);
	}

	@Override
	public ActionForward modifyStudent(HttpServletRequest request) {
		int studentNo = Integer.parseInt(request.getParameter("studentNo"));
		String name = request.getParameter("name");
		int korean = Integer.parseInt(request.getParameter("korean"));
		int english = Integer.parseInt(request.getParameter("english"));
		int math = Integer.parseInt(request.getParameter("math"));
		StudentDto student = StudentDto.builder()
				               .studentNo(studentNo)
				               .name(name)
				               .korean(korean)
				               .english(english)
				               .math(math)
				               .build();
		
		int updateCount = studentDao.updateStudent(student);
		String view = null;
		if(updateCount == 1) {
		      view = request.getContextPath() + "/student/list.do"; 
		    } else if(updateCount ==0) {
		      view = request.getContextPath() + "/student/list.do";
		    }
		return new ActionForward(view, true);
	}

	@Override
	public ActionForward getList(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward getByScore(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
