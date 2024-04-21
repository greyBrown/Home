
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="<%=request.getContextPath()%>"/>
<c:set var="dt" value="<%=System.currentTimeMillis()%>"/>

<jsp:include page="../layout/header.jsp">
  <jsp:param value="${blog.blogNo}번 블로그" name="title"/>
</jsp:include>

<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<h1 class="title">블로그 상세화면</h1>

<div>
  <span>작성자</span>
  <span>${blog.user.email}</span>
</div>

<div>
  <span>조회수</span>
  <span>${blog.hit}</span>
</div>

<div>
  <span>제목</span>
  <span>${blog.title}</span>
</div>

<div>
  <span>내용</span>
  <span>${blog.contents}</span>
</div>

<div>
<button type=button class="btn btn-secondary btn-modify">수정</button>
</div>

<div>
 <button type=button class="btn btn-warning btn-remove">삭제</button>
</div>


<hr>

<form id="frm-comment">
  <textarea id="contents" name="contents"></textarea>
  <input type="hidden" name="blogNo" value="${blog.blogNo}">
  <c:if test="${not empty sessionScope.user}">  
    <input type="hidden" name="userNo" value="${sessionScope.user.userNo}">
  </c:if>
  <button type="button" id="btn-comment-register">댓글등록</button>
</form>

<hr>

<div id="comment-list"></div>
<div id="paging"></div>


<script>

const fnCheckSignin = () => {
    if('${sessionScope.user}' === ''){
      if(confirm('Sign In 이 필요한 기능입니다. Sign In 할까요?')){
        location.href = '${contextPath}/user/signin.page';
      }
    }
  }

const fnRegisterComment = () => {
  $('#btn-comment-register').on('click', (evt) => {
	  fnCheckSignin();
    $.ajax({
      // 요청
      type: 'POST',
      url: '${contextPath}/blog/registerComment.do',
      data: $('#frm-comment').serialize(),   // <form> 내부의 모든 입력을 파라미터 형식으로 보낼 때 사용, 입력 요소들은 name 속성을 가지고 있어야 함
      // 파라미터로 보내면 -> request, @requestparam, model 이 셋 중 하나로 받겠죱?
      //응답
      dataType: 'json',
      success: (resData) => { // resData = {"insertCount": 1}
      	if(resData.insertCount === 1) {
      		  alert('댓글이 등록되었습니다.');
      		  $('#contents').val(''); // 댓글 등록 후 댓글란 초기화
      		  // 댓글 목록보여주는 함수
      		fnCommentList();
      	} else {
      		alert('댓글 등록이 실패했습니다');
      	}
      },
      error: (jqXHR) => {
      	alert(jqXHR.statusText + '(' + jqXHR.status + ')');
      }
    })
  })
}

// 전역 변수

const fnModifyBlog = () => {
    $(document).on('click', '.btn-modify', (evt) => {
       location.href = '${contextPath}/blog/modify.page?blogNo=' + ${blog.blogNo};
    })
  }




var page = 1;

const fnCommentList = () => {
	$.ajax({
		type: 'GET', 
		url: '${contextPath}/blog/comment/list.do',
		data: 'blogNo=${blog.blogNo}&page=' + page,  
		dataType: 'json',
		success: (resData) => { //resData = {"commentList:" [], "paging" : "< 1 2 3 4 5 >"}
			let commentList = $('#comment-list');
		  let paging = $('#paging');
		  commentList.empty();
		  paging.empty();
		  if(resData.commentList.length === 0){
			  commentList.append('<div>첫 번째 댓글의 주인공이 되어 보세요</div>');
			  paging.empty();
			  return;
		  }
		  // return + if = else if . if에 걸린 친구들은 return 때문에 여기까지 못내려옴. 들여쓰기가 너무 많아지는 것 같아 이렇게~
		   $.each(resData.commentList, (i, comment) => {
			  let str = '';
			  // 원글과 댓글의 구분 : 댓글은 들여쓰기(댓글 여는 <div>)
			  if(comment.depth === 0) {
				  str += '<div>';
			  } else {
				  str += '<div style="padding-left: 32px;">'         //16px 기본 픽셀로 한글자 정도여서 두글자 들여쓰기로 조정.
			  }
			  // 댓글 내용 표시
			    str += '<span>'
			    str += comment.user.email;
			    str += '(' + moment(comment.createDt).format('YYYY.MM.DD') + ')';
			    str += '</span>';
			    str += '<div>' + comment.contents + '</div>';
			  // 답글 버튼(원글에만 답글 버튼이 생성됨)
			  if(comment.depth === 0) {
				  str += '<button type="button" class="btn btn-success btn-reply">답글</button>';
			  }
			  // 삭제 버튼 (작성한 댓글에만 삭제 버튼이 생성됨)
			  if(Number('${sessionScope.user.userNo}') === comment.user.userNo){ // === 는 타입도 맞춰야 하기때문에 Number() 로 싸준다
				  str += '<button type="button" class="btn btn-danger btn-remove" data-comment-no="'+comment.commentNo+'">삭제</button>';
			  }
			  /******************************답글 입력 화면***********************************/
			   if(comment.depth === 0) { // if 문 아래 아래 코드들을 넣어줌으로써 2차 답글은 달지 못하게 된다. depth === 0 즉 최초댓글에 대한  답댓만 달 수 있음!
  			  str += '<div>';
      			  str += '<form class="frm-reply">'
      			  str += '<input type="hidden" name="groupNo" value="'+ comment.groupNo +'">';
      			  str += '<input type="hidden" name="blogNo" value="${blog.blogNo}">'; 
      			  str += '<input type="hidden" name="userNo" value="${sessionScope.user.userNo}">'; 
      			  str += '<textarea name="contents" placeholder="답글 입력"></textarea>';
      			  str += '<button type="button" class="btn btn-warning btn-register-reply">작성완료</button>';
      			  str += '</form>'
  			  str += '</div>';
        }
			  /*******************************************************************************/
			  
			  // 댓글 닫는 <div>
			    str += '</div>';
			  // 목록에 댓글 추가
			  commentList.append(str);
		  })
		  // 페이징표시
	      paging.append(resData.paging);
		},
		error: (jqXHR) => {
			alert(jqXHR.statusText + '(' + jqXHR.status + ')');
		}
	})
}

const fnRemoveReply = () => {
    $(document).on('click', '.btn-remove', (evt) => {
    	 location.href = '${contextPath}/blog/comment/removeComment.do?commentNo=' + evt.target.dataset.commentNo + '&blogNo=' + ${blog.blogNo};
    })
  }
  

  


const fnPaging = (p) => {
	page = p;
	fnCommentList();
 }
 
const fnRegisterReply = () => {
$(document).on('click', '.btn-register-reply', (evt) => {
	   fnCheckSignin();
     $.ajax({
    	 type: 'POST',
    	 url: '${contextPath}/blog/comment/registerReply.do',
    	 data: $(evt.target).closest('.frm-reply').serialize(),
    	 dataType: 'json',
    	 success: (resData) => {
    	   if(resData.insertReplyCount === 1){
    		   alert('답글이 등록되었습니다.');
    		   $(evt.target).prev().val('');
    		   fnCommentList();
    	   } else {
    		   alert('답글 등록이 실패하였습니다.');
    	   }
    	 },
    	 error: (jqXHR) => {
    		 alert(jqXHR.statusText + '(' + jqXHR.status + ')');
    	 }
     })
   })
}

 
$('#contents').on('click', fnCheckSignin); // 이렇게 이벤트로 함수 호출할때 () 안넣는다. 넣어놓고 뭐야뭐야 하지 않기...^^
fnRegisterComment();
fnCommentList();
fnRegisterReply();
fnRemoveReply();
fnModifyBlog();

</script>

<%@ include file="../layout/footer.jsp" %>
