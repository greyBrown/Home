<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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

<form>
<div>학번 <input type="text" name="studentNo"></div>
<div>이름 <input type="text" name="studentName"></div>
<div>국어 <input type="text" name="korean"></div>
<div>영어 <input type="text" name="english"></div>
<div>수학 <input type="text" name="math"></div>
<div>평균 <input type="text" class="read" name="average" readonly></div>
<div>학점 <input type="text" class="read" name="grade" readonly></div>

<hr>

<span><button type="submit">수정하기</button> <button type="button">목록보기</button></span> 

</form>






</body>
</html>