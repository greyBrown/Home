<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>

<style>
span > *{
margin:2px;
}

.read{
border:1px solid gray;
background-color:lightgray;
}
</style>

</head>
<body>

<h1>학생 상세 조회</h1>

<form id="frm-btn"
        method="GET"
        action="${contextPath}/student/modify.do">
<div>학번 <input type="text" name="studentNo"  ></div>
<div>이름 <input type="text" name="name"></div>
<div>국어 <input type="text" name="korean"></div>
<div>영어 <input type="text" name="english"></div>
<div>수학 <input type="text" name="math"></div>
<div>평균 <input type="text" class="read" name="average" readonly></div>
<div>학점 <input type="text" class="read" name="grade" readonly></div>

<hr>

<span><button type="submit" id="btn-modify">수정하기</button> 
<button type="button" id="btn-list">목록보기</button></span> 

</form>

<script>
  const frmBtn = $('#frm-btn');
  const btnModify = $('#btn-modify');
  const btnList = $('#btn-list');
  
  btnModify.on('click', (evt)=>{
	  frmBtn.attr('action', '${contextPath}/student/modify.do');
	  frmBtn.submit();
  })
  
  btnList.on('click', (evt)=>{
		  frmBtn.attr('action', '${contextPath}/student/list.do');
		  frmBtn.submit();
  })

</script>




</body>
</html>