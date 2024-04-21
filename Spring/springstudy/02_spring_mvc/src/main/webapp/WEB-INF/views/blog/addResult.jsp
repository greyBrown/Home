<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

 <div>${blogDto.blogNo}번 게시글</div>        <!-- BlogDto -> blogDto 로 자동으로 변환되어 저장되었다. -->
 <div>${blogDto.title}</div>
 
 <hr>
 
  <div>${blog.blogNo}번 게시글</div>          <!-- blogDto 이름을 blog로 변경하였다. -->
  <div>${blog.title}</div>
 
</body>
</html>