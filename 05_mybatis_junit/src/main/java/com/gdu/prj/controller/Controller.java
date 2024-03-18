package com.gdu.prj.controller;

import java.io.IOException;

import com.gdu.prj.common.ActionForward;
import com.gdu.prj.service.StudentService;
import com.gdu.prj.service.StudentServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
   
	private StudentService studentService = new StudentServiceImpl(); 
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	 String requestURI = request.getRequestURI();             
	    String contextPath = request.getContextPath();           
	    String urlMapping = requestURI.substring(requestURI.indexOf(contextPath) + contextPath.length());
	    
	    ActionForward actionForward = null;
	    
	 
	    switch(urlMapping) {
	    case"/student/list.do":
	    	actionForward = new ActionForward("/student/list.jsp", false);
	        break;
	    
	    case"/student/write.do":
	    	actionForward = new ActionForward("/student/write.jsp", false);
	        break;
	        
	    case"/student/register.do":
	    	actionForward = studentService.addStudent(request);
	    	
	    case"/student/modify.do":
	    	actionForward = studentService.modifyStudent(request);
	    	
	    	
	    	
	    	
	    	
	    }
	    
	    	
	    
	    
	    if(actionForward != null) {
		    if(actionForward.isRedirect()) {
		      response.sendRedirect(actionForward.getView());
		    } else {
		      request.getRequestDispatcher(actionForward.getView()).forward(request, response);
		    }
		  }
	
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
