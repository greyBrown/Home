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
<h1>학생 관리</h1>

<div><button type="button" id="insertbtn">신규학생등록</button></div>

<hr>

<div>평균 
<input type="text" name="begin"> ~ <input type="text" name="end"> 
<button type="button">조회</button>
<button type="button">전체조회</button>
</div>

<hr>

<table border="1">
<thead>
<td>학번</td>
<td>성명</td>
<td>국어</td>
<td>영어</td>
<td>수학</td>
<td>평균</td>
<td>학점</td>
<td>버튼</td>
</thead>
<tbody>
<td colspan="8">등록된 학생이 없습니다.</td>
</tbody>
<tfoot>
<td colspan="5">전체평균</td>
<td></td>
<td colspan="2"></td>


</tfoot>

</table>

<script>
$('#insertbtn').on('click', (evt)=>{
	  location.href = "${contextPath}/student/write.do";
})
</script>

</body>
</html>