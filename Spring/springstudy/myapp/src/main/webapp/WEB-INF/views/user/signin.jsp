<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="dt" value="<%=System.currentTimeMillis()%>"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<jsp:include page="../layout/header.jsp">
  <jsp:param value="Sign In" name="title"/>
</jsp:include>

<!-- model로 저장한것은 el로 확인할 수 있다. model에 저장된 것은 한번만 이동하는 1회용이다. 즉 여기까지만 이동하고, 더 이동시키고 싶으면 처리를 해야함-->

 <h1 class="title">Sign In</h1>
  
  <div>
    <form method="POST"
          action="${contextPath}/user/signin.do">
   <!-- 페이지를 구분하고 있음! 로직이 있거나 처리해야할 작업이 있으면 do, 단순한 페이지 이동이면 page -->
      <div>
        <label for="email">아이디</label>
        <input type="text" id="email" name="email" placeholder="example@naver.com">
      </div>
      <div>
        <label for="pw">비밀번호</label>
        <input type="password" id="pw" name="pw" placeholder="●●●●">
      </div>
      <div>
        <input type="hidden" name="url" value="${url}">
        <button type="submit">Sign In</button>
      </div>
      <div>
        <a href="${naverLoginURL}">
          <img src ="${contextPath}/resources/2021_Login_with_naver_guidelines_Kr/btnG_아이콘원형.png">
        </a>
      </div>
    </form>
  </div>
  
 <%@ include file="../layout/footer.jsp"%>