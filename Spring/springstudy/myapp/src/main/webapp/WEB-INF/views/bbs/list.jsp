<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="dt" value="<%=System.currentTimeMillis()%>"/>

<jsp:include page="../layout/header.jsp"/>

<style>
  .blind {
    display: none;
  }
</style>

 <h1 class="title">BBS</h1>
 
 <a href="${contextPath}/bbs/write.page">작성하러가기</a>
 <!-- spa가 아니니까 이동~ 페이지 안바뀌고 여기 목록에서 작성한다면 ajax나 fetch 같은 비동기통신 나와야한다. -->
 
 <div>
 <!--  검색할 때 쓰는 method 는 GET. 검색에 POST 쓰지 않는다. -->
  <form method="GET"
        action="${contextPath}/bbs/search.do">
    <div>
      <select name="column">
      <!-- value 대문자로 적은 이유 : 이걸 쿼리문에 심겠다는 빅픽쳐. 여기서부터 심어놓습니당 <<< 쿼리문을 짜보면 테이블 별명이 필요하다는 걸 알게됨. 여기에 넣어준다.-->
        <option value="U.EMAIL">작성자</option>
        <option value="B.CONTENTS">내용</option>
      </select>    
      <input type="text" name="query" placeholder="검색어입력">
      <button type="submit">검색</button>
    </div>   
  </form>
 </div>
 
 <table border="1">
    <thead>
    <tr>
    <td>순번</td>
    <td>작성자</td>
    <td>내용</td>
    <td>작성일자</td>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${bbsList}" var="bbs" varStatus="vs">
    <tr class="bbs">
     <td>${beginNo - vs.index}</td>
     <c:if test="${bbs.state == 1}">
     <td>${bbs.user.email}</td>
     <td>
      <c:forEach begin="1" end="${bbs.depth}" step="1">&nbsp;&nbsp;</c:forEach>
      <c:if test="${bbs.depth != 0}">[Re]</c:if>
     ${bbs.contents}
     <!-- 내가 작성한 "답글" 버튼은 보이지 않게 해보기 -->
     <!-- 내가 작성한 것만 삭제 버튼 보이게 해보기 -->
     <!-- 아악 거의 근접했는데..주말동안 열공해봅시다 ㅠㅠ -->
     <c:if test="${bbs.user.userNo != sessionScope.user.userNo}">
     <button type="button" class="btn-reply">답글</button>
     </c:if>
     <c:if test="${bbs.user.userNo == sessionScope.user.userNo}">
     <button type="button" class="btn-remove">삭제</button>
     <input type="hidden" value="${bbs.bbsNo}">
     </c:if>
     </td>
     <td>
          <fmt:formatDate value ="${bbs.createDt}" pattern="yyyy. MM. dd. HH:mm:ss"/>
     </td>
     </c:if>
     <c:if test="${bbs.state ==0}">
     <td colspan="3">삭제된 게시글입니다.</td>
     </c:if>
    </tr>
    <tr class="write blind">
    <td colspan="4">
    <form method="POST"
          action="${contextPath}/bbs/registerReply.do">
      <div>
        <span>답글작성자</span>
        <span>${sessionScope.user.email}</span>
     </div>  
         
     <div>
        <textarea class="contents" name="contents" placeholder="답글을 입력하세요"></textarea>
     </div>
     <div>
       <input type="hidden" name="userNo" value="${sessionScope.user.userNo}">
       <!-- 원글의 depth / groupNo/ groupOrder 를 이용하여 답글을 구성해야함 -->
       <!-- registerReply는 update를 먼저, insert를 나중에 하는 방식으로 진행되게 됨. -->
       <input type="hidden" name="depth" value="${bbs.depth}">
       <input type="hidden" name="groupNo" value="${bbs.groupNo}">
       <input type="hidden" name="groupOrder" value="${bbs.groupOrder}">
       <button type="submit">작성완료</button>
     </div>   
    </form>
    </td>
    </tr>
    </c:forEach>
    </tbody> 
 </table>
 
 <div>${paging}</div>
 
 <script>
 
 const fnBtnRemove = () => {
	    $('.btn-remove').on('click', (evt) => {
	      if(confirm('게시글을 삭제할까요?')) {
	        location.href = '${contextPath}/bbs/removeBbs.do?bbsNo=' + $(evt.target).next().val(); // 저번엔 데이터속성으로 btn에 줘서 삭제했었음! 
	      }
	    })
	  }
 
  const fnCheckSignin = () => {
	  if('${sessionScope.user}' === ''){
		  if(confirm('Sign In 이 필요한 기능입니다. Sign In 할까요?')){
			  location.href = '${contextPath}/user/signin.page';
		  }
	  }
  }
 
  const fnBtnReply = () => {
	  $('.btn-reply').on('click', (evt)=>{
		  // Sign In 체크
		   fnCheckSignin();
		  // 답글 작성 화면 조작하기
		  // 답글버튼의 부모를 찾아서, 그 부모의 인접형제를 찾고 해당형제의 블라인드 속성을 없애주면 답글 밑의 입력락만 보인다.
		  // -> 모든 답글들의 textarea 가 동동 떠나니는게 아니라 내가 입력한 글만 '답글을 입력하세요'가 뜨게 만들겠다!
		 let write = $(evt.target).closest('.bbs').next();
		  if(write.hasClass('blind')){
			  $('.write').addClass('blind');  // 모든 답글 작성 화면 닫은 뒤 
			  write.removeClass('blind');     // 클릭한 답글 작성 화면만 열기
		  } else{
			  write.addClass('blind');        // 답글 작성 화면이 열려있다면 닫겠다.
		  }
	  })
  }
  
 const fnInsertBbsCount = () => {
	    let insertBbsCount = '${insertBbsCount}';
	    if(insertBbsCount !== '') {
	      if(insertBbsCount === '1') {
	        alert('BBS 원글이 등록되었습니다.');
	      } else {
	        alert('BBS 원글이 등록되지 않았습니다.'); 
	      }
	    }
	  }
 
 const fnInsertReplyCount = () => {
     let insertReplyCount = '${insertReplyCount}';
     if(insertReplyCount !== '') {
       if(insertReplyCount === '1') {
         alert('BBS 답글이 등록되었습니다.');
       } else {
         alert('BBS 답글이 등록되지 않았습니다.'); 
       }
     }
   }
 
 fnBtnRemove();
 $('.contents').on('click', fnCheckSignin); // 혹시 로그인이 풀려있는 상황을 방지하기 위해 누르기 전에 체크 한번 더
 fnBtnReply();
 fnInsertBbsCount();
 fnInsertReplyCount();
 </script>

 <%@include file="../layout/footer.jsp" %>