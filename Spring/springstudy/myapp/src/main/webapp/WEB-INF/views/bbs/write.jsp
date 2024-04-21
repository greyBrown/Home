<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="dt" value="<%=System.currentTimeMillis()%>"/>


<jsp:include page="../layout/header.jsp"/>

 <h1 class="title">BBS 작성화면</h1>
 
 <!-- 작성만 write에서 하고 나머지는 다 목록에서 한다. 간단한 게시판임! -->
 <!-- 작성할때 즈에발 중요한것만 정렬해놓고 작업해요 뭐 물어봤는데 탭 20개고 이러면 캭 -->
 
 <form id="frm-bbs-regiser"
       method="POST"
       action = "${contextPath}/bbs/register.do">
       
 <div>
  <span>작성자</span>
  <span>${sessionScope.user.email}</span>
 </div>      
 
 <div>
  <textarea id="contents" name="contents" placeholder="내용을 입력하세요"></textarea>
 </div>
 
 <div>
 <input type="hidden" name="userNo" value="${sessionScope.user.userNo}">
 <button type="submit">작성완료</button>
 <a href="${contextPath}/bbs/list.do"><button type="button">작성취소</button></a>
 </div>
 
 </form>
 
 <script>

 // 파라미터 자체가 안오면 null, 파라미터 값이 안오면 빈 문자열! 대애충 이러이러한 구조일 거란건 맞았다. 디테일 연습.... val이 제이쿼리였나..?
  const fnRegisterBbs = (evt) =>{
	  if(document.getElementById('contents').value === ''){
		  alert('내용 입력은 필수입니다.');
		  evt.preventDefault();
		  return;
	  }
  }
  
  document.getElementById('frm-bbs-regiser').addEventListener('submit', (evt)=>{
	  fnRegisterBbs(evt);
  })
  
 
 
 </script>
 

 <%@include file="../layout/footer.jsp" %>