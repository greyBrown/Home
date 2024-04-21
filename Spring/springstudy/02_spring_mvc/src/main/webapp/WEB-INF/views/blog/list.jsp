<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>블로그 목록</title>
<style>
.blog{
 width: 200px;
 cursor: pointer;
 background-color: yellow;
}
</style>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
</head>
<body>
    
  <c:forEach items="${blogList}" var="blog" varStatus="vs">
    <div class="blog">
      <span>${vs.index}</span>
      <span class="blog-no">${blog.blogNo}</span>
      <span>${blog.title}</span>
    </div>
  </c:forEach>
  
  <script type="text/javascript">
   
   $('.blog').on('click', function(evt){              //jQuery를 사용하면 배열에도 이벤트를 바로 만들 수 있다. 배열 이벤트는 jQ가 훨씬 편함!
	  let blogNo = $(this).find('.blog-no').text();     // 화살표 함수는 this 사용이 불가능해서 익명함수로 바꾸고 evt.target -> this 로 변경
	 location.href ='${contextPath}/blog/detail.do?blogNo=' + blogNo;
   });
  
  
  </script>



</body>
</html>