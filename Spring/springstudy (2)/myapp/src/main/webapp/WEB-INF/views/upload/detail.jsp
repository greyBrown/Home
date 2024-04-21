<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<c:set var="dt" value="<%=System.currentTimeMillis()%>"/>

<jsp:include page="../layout/header.jsp">
  <jsp:param value="${upload.uploadNo}번 업로드" name="title"/>
</jsp:include>

<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<h1 class="title">업로드 상세화면</h1>

<div>
  <span>작성자</span>
  <span>${upload.user.email}</span>
</div>

<div>
  <span>제목</span>
  <span>${upload.title}</span>
</div>

<div>
  <span>내용</span>
  <span>${upload.contents}</span>
</div>

<div>
  <span>작성일자</span>
  <span>${upload.createDt}</span>
</div>

<div>
  <span>최종수정일</span>
  <span>${upload.modifyDt}</span>
</div>

<div>
  <c:if test="${not empty sessionScope.user}">  
    <c:if test="${sessionScope.user.userNo == upload.user.userNo}">
      <form id="frm-btn" method="POST">  
        <input type="hidden" name="uploadNo" value="${upload.uploadNo}">
        <!-- form에 들어있는 상태 -> 즉 submit으로 이동한다. -->
        <button type="button" id="btn-edit" class="btn btn-warning btn-sm">편집</button>
        <button type="button" id="btn-remove" class="btn btn-danger btn-sm">삭제</button>
      </form>
    </c:if>
  </c:if>
</div>

<hr>

<!-- 첨부 목록 -->
<h3>첨부 파일 다운로드</h3>
<div>
  <c:if test="${empty attachList}">
  <div>첨부 없음</div>
  </c:if>
  <c:if test="${not empty attachList}">
  <c:forEach items="${attachList}" var="attach">
  
  <div class="attach" data-attach-no="${attach.attachNo}">
  <c:if test="${attach.hasThumbnail == 1}">
  <img src="${contextPath}${attach.uploadPath}/s_${attach.filesystemName}">
  </c:if>
  <c:if test="${attach.hasThumbnail == 0}">
  <img src="${contextPath}/resources/images/attach.png" width="96px">
  </c:if>
  <a>${attach.originalFilename}</a>
  </div>
  </c:forEach>
  <div> 
  
    <a id="download-all" href="${contextPath}/upload/downloadAll.do?uploadNo=${upload.uploadNo}">모두 다운로드</a>
    <!-- 이 공간은 upload의 상세보기! model에 upload와 attach 둘다 있음  -->
  </div>
  </c:if>
</div>

<script>

const fnDownload = () => {
	  $('.attach').on('click', (evt) => {
	    if(confirm('해당 첨부 파일을 다운로드 할까요?')) {
	      location.href = '${contextPath}/upload/download.do?attachNo=' + evt.currentTarget.dataset.attachNo;
	   // 자바스크립트 구역은 모델 받아오는 구역이 아님. EL 아니라 evt.target.dataset.attachNo; 이거 쓰는 이유 EL 주석내에서 ㄴㄴㄴ!!! 무심코 쓴거 때문에 자꾸 오류난다
	    }
	  })
	}

const fnDownloadAll = () => {
	  document.getElementById('download-all').addEventListener('click', (evt) => {
	    if(!confirm('모두 다운로드 할까요?')) {
	        <%--confirm 임. 만약 yes를 누른다면/upload/downloadAll.do?uploadNo=$upload.uploadNo 작성해놓은 이게 작동되고 아니요를 누르면 preventDefault--%>
	      evt.preventDefault();
	       return; // 리턴은 사실 있어도 그만 없어도 그만. 리턴은 다음 코드 실행을 막는건데 여기 다음코드가 없다.
	    }
	  })
	}


	// 전역 객체
	var frmBtn = document.getElementById('frm-btn');

	const fnEditUpload = () => {
	  document.getElementById('btn-edit').addEventListener('click', (evt) => {
	    frmBtn.action = '${contextPath}/upload/edit.do';
	    frmBtn.submit();
	  })
	}

	const fnAfterModifyUpdate = () => {
	  const updateCount = '${updateCount}';
	  if(updateCount !== '') {
	    if(updateCount === '1') {
	      alert('게시글이 수정되었습니다.');
	    } else {
	      alert('게시글이 수정되지 않았습니다.');
	    }
	  }
	}

	fnEditUpload();
	fnAfterModifyUpdate();
	fnDownload();
	fnDownloadAll();

</script>

<%@ include file="../layout/footer.jsp" %>