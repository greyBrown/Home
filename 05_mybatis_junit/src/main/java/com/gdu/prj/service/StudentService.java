package com.gdu.prj.service;

import com.gdu.prj.common.ActionForward;

import jakarta.servlet.http.HttpServletRequest;

public interface StudentService {

	ActionForward addStudent(HttpServletRequest request);
	ActionForward modifyStudent(HttpServletRequest request);
	ActionForward getList(HttpServletRequest request);
	ActionForward getByScore(HttpServletRequest request);
	
	
	
}
