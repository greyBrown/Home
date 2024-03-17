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
</head>
<body>

<h1>신규학생등록화면</h1>


<form id="frm-register"
        method="GET"
        action="${contextPath}/student/register.do">
<div>이름 <input type="text" name="name"></div>
<div>국어 <input type="text" name="korean"></div>
<div>영어 <input type="text" name="english"></div>
<div>수학 <input type="text" name="math"></div>
<hr>
<button type="submit">작성완료</button>
<button type="reset">다시작성</button>
<button type="button">목록보기</button>

</form>





</body>
</html>