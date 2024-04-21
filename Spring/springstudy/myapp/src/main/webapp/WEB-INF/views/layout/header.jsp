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
<!-- 반응형 작업할 때 필요한 메타태그. htmlstudy면 다있으니까 긁어옴 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0"> 

<!-- 페이지마다 다른 제목 -->
<title>
<c:choose>
  <c:when test="${empty param.title}">Welcome</c:when> 
  <c:otherwise>${param.title}</c:otherwise>
</c:choose>
</title>
  <!-- param.title==null 도 가능하지만 훨씬 간단한 문법이 empty 반대는 not empty -->
  <!-- param.title el 이 지원하는 parameter 불러오는 명칭! 이것도 jsp 어딘가에서 했었다.. -->


<!-- include libraries(jquery, bootstrap) -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">

<!-- include summernote css/js -->
<link rel="stylesheet" href="${contextPath}/resources/summernote-0.8.18-dist/summernote.min.css">
<script src="${contextPath}/resources/summernote-0.8.18-dist/summernote.min.js"></script>
<script src="${contextPath}/resources/summernote-0.8.18-dist/lang/summernote-ko-KR.min.js"></script>

<!-- header footer 나눠줄 거임 jspstudy/02_jsp/src/main/webapp/pkg03_include/ 여기서 했었던 내용 -->
<!-- include custom css/js css 파일 따로 빼주고 경로 링크 걸어줌~~~ -->
<link rel="stylesheet" href="${contextPath}/resources/css/init.css?dt=${dt}">
<link rel="stylesheet" href="${contextPath}/resources/css/header.css?dt=${dt}">


</head>
<body>



    <div class="header-wrap">
    
     <div class="logo"></div>
     <div class="user-wrap">
            <!-- Sign In 안 된 경우 -->
         <c:if test="${sessionScope.user == null}">  
          <a href="${contextPath}/user/signin.page"><i class="fa-solid fa-arrow-right-to-bracket"></i> Sign In </a>
          <a href="${contextPath}/user/signup.page"><i class="fa-solid fa-user-plus"></i> Sign Up </a>
        </c:if>
          <!-- Sign In 된 경우 -->
        <c:if test="${sessionScope.user != null}">
          ${sessionScope.user.name}님 반갑습니다
          <!-- serviceImpl에서 session에 저장해놓은 그 정보 -->
          <a href="${contextPath}/user/signout.do">로그아웃</a>
          <!-- 로그아웃 알아서 해봐요! 이제 네이버로그인하러 갑니다... -->
          <a href="${contextPath}/user/leave.do">회원탈퇴</a>
        </c:if>
       </div>
     <div class="gnb-wrap">
      <ul class="gnb">
        <li><a href="${contextPath}/bbs/list.do"> 계층형 게시판 </a></li>
        <li><a href="${contextPath}/blog/list.do"> 댓글형 게시판</a></li>
        <li><a href="${contextPath}/"> 첨부형 게시판</a></li>
        <li><a href="${contextPath}/"> 마이페이지</a></li>
      </ul>
     </div>
    <div> 현재 sessionId : <%=session.getId() %></div>
    <!-- jsp가 제공하는 9가지 내장 객체(이미 선언되어 있어 선언할 필요 없는) session, request, response... -->
    <!-- 프로젝트를 구동시켜 작업이 시작되면 sessionId가 발생한다.
    1. 로그인을 해도 세션이 변하지 않음(세션이 유지되고 있다)
    2. 로그아웃을 하면 세션이 바뀐다. (세션이 변경되었다) -> 로그아웃에 있는 세션 초기화 메소드 session.invalidate()). 이제 새로운 작업(세션)이라 보게 된다.
    로그인 - 로그아웃 사이는 동일 세션이다. 로그인한 사람만 해당 세션을 사용한다. 세션에 뭐가 이것저것 올라가도 그 사람의 것임.
    (세션의 유지기간 : 브라우저가 꺼지기 전까지. 브라우저 안닫고 프로젝트 껏다 켜도 세선은 동일함. 브라우저 닫고 다시 프로젝트 시작하면 세션은 바뀐다.)
   =  사용자의 로그인-로그아웃 까지의 상태를 알고 싶다면 세션ID를 알고 있어야한다.
   -> 히스토리를 남길때 이메일만 가지고 사용자의 접속기록을 남기는 건 불가능. 한 사람이 여러번 들락날락 할 것이기 때문...
   (브라우저를 꺼버려서 하는 로그아웃(로그아웃 시간이 안남음)이 문제. 언제 로그아웃을 하는지 DB가 잘 모름. (로그아웃 = 로그인기록에 로그아웃 시간 UPDATE) 
   브라우저 끄고 나갔다가 다시 로그인함. 또 브라우저 끄고 나감. 그럼 user1 24.04.04 null 이런것만 계속 insert 됨... 이때 이메일만 남아아있으면 로그아웃 버튼을 눌러도, 언제적 로그인을 로그아웃 하겠다는건지 모르게 됨
   이때 세션 ID를 사용함. 로그아웃할 때 가진 세션값 = 로그인할 때 가진 세션값 을 구별해서 같은 세션값을 가진 로그인기록에 로그아웃 기록을 업데이트해준다.)
   근데 로그아웃 시간은 대애애부분 null 값으로 채워지기 때문에 그냥 칼럼 안만드는 경우도 부지기수다 ㅋㅋㅋㅋ 보통 로그아웃 타임 실제구현 안한다.
   다들 로그아웃 버튼을 누르지 않기 때문...
   + 세션 만료기간 디폴트 30분.
   -->
    
    </div>
    
    <div class="main-wrap">
    <!-- main-wrap 은 header 에서 닫히지 않는다. -->
   
  
</body>
</html>