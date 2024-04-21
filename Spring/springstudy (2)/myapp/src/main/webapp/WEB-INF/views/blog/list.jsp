<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="dt" value="<%=System.currentTimeMillis()%>"/>


<jsp:include page="../layout/header.jsp"/>
 
<style>

.blog {
   width: 640px;
   height: 180px;
   margin: 10px auto;
   border: 1px solid gray;
   cursor: pointer;
 }
 .blog > span {
   color: tomato;
   display: inline-block;
   box-sizing: border-box;
 
 }
 .blog > span:nth-of-type(1) {width : 100px;}
 .blog > span:nth-of-type(2) {width : 250px;}
 .blog > span:nth-of-type(3) {width : 50px;}
 .blog > span:nth-of-type(4) {width : 150px;}

</style>

 <h1 class="title">블로그 목록</h1>
 
<a href="${contextPath}/blog/write.page">블로그작성</a>

<div id="blog-list">


</div>



<script>

// 전역 변수
var page = 1;
var totalPage = 0; // 0으로 초기화 후 ajax success 부분에서 갱신


const fnGetBlogList = () => {
	
	// page 에 해당하는 목록 요청
	$.ajax({
	  // 요청
	  type: 'GET',
	  url: '${contextPath}/blog/getBlogList.do',                // ?page=' + page, 여기 이렇게 박아도 좋지만, url 과 파라미터를 분리할 수 있다면 분리하는 게 좋음. 분리 어떻게 하죠? 바로 아래 data에 적어주면 된다.
		data : 'page=' + page,	  
	  // 응답
	  dataType: 'json',
	  success: (resData) => {     // resData = {"blogList": [], "totalPage": 10} 스크롤이 증가하는건 최대 totalPage 까지임.
			totalPage = resData.totalPage;
			 $.each(resData.blogList, (i, blog) => {
	    	let str = '<div class="blog" data-user-no="'+ blog.user.userNo +'" data-blog-no="'+ blog.blogNo +'">';
	    	str += '<span>' + blog.title + '</span>';
	    	str += '<span>' + blog.user.email + '</span>';
	    	str += '<span>' + blog.hit + '</span>';
	    	str += '<span>' + moment(blog.createDt).format('YYYY.MM.DD') + '</span>';
	    	// 날짜 형식 지정을 위한 moment.js 사용
	    	str += '</div>';
	    	$('#blog-list').append(str);   // #blog-list에 만들어 놓은 str 삽입
	    })
	  },
	  error: (jqXHR) => {
		  alert(jqXHR.statusText + '(' + jqXHR.status + ')');
	  }
	});
	
	
	
}

const fnScrollHandler = () => {
	
	// 스크롤이 바닥에 닿으면 page 증가(최대 totalPage 까지) 후 새로운 목록 요청
	
	// 타이머 id (동작한 타이머의 동작 취소를 위한 변수)
	var timerId; //undefined, boolean 의 의미로는 false
	
	
	 /*
    스크롤 이벤트 발생 → setTimeout() 함수 동작 → 목록 가져옴 → setTimeout() 함수 동작 취소
  */
	
	$(window).on('scroll', (evt) => {  // 스크롤1px 움직일때마다 요청됨. -> 너무 많이 동작한다는 문제점이 생김 -> 이벤트를 몇초간 간격을 두고 동작하게 함.

		if(timerId) {                  //timerId 가 undefined 이면 false, 아니면 true
                                   //timerId 가 undefined 이면 setTimeout() 함수가 동작한 적 없음. 즉 timerId가 true이려면 undefined가 아니여야 함(=이게 작동한 적이 있다.)
         clearTimeout(timerId);    // setTimeout() 함수 동작을 취소함 -> 목록을 가져오지 않는다. 타이머의 장점 동작을 취소시킬 수 있음          
       }  
		
		// 500밀리초(0.5초) 후에 () => {}가 동작하는 setTimeOut 함수
		timerId = setTimeout(() => {
	/*-------------------------------------------------------------------------------*/
		 let scrollTop = $(window).scrollTop();
     let windowHeight = $(window).height();
     let documentHeight = $(document).height();
		  
	  if( (scrollTop + windowHeight + 50) >= documentHeight){  // 스크롤바와 바닥 사이 길이가 50px 이하인 경우
		  if(page > totalPage) {      // page가 total 보다 커질 때, 즉 더이상 불러올 페이지가 없을 때 ajax 동작하지 않게 멈춤.
			  return;
		  }
		  page++;
		  fnGetBlogList();
	  }
	 /*-------------------------------------------------------------------------------*/
	// 스크롤 이벤트가 너무나 많이 발생함 -> 0.5초후에 목록을 가져올것. 한번 가져왔다면 더 동작하지 말 것(setTimeOut을 사용). 이렇게 처리해봄.
		}, 500);
	})
	/*  window.addEventListener('scroll', (evt) => {
	 *  })   jQuery를 쓰지 않는다면ver.
	 */
}
  
  const fnBlogDetail = () => {
	  
	  $(document).on('click', '.blog', (evt) => {
		 // <div class="blog"> 중 클릭 이벤트가 발생한 <div> : 이벤트 대상
	   //  alert(evt.target.dataset.blogNo)=alert($(evt.target).data('blogNo'))
	   //  alert(evt.target.dataset.userNo)=alert($(evt.target).data('userNo'))
	   
	   // 내가 작성한 블로그는 /detail.do 요청 (조회수 증가가 없음)
	   // 남이 작성한 블로그는 /updateHit.do 요청 (조회수 증가함)
	   // resData에서 data-set 을 심어주는 것부터 해야함 ajax의 영향권이 아니기에 여기엔 그 데이터가 없기 때문.
     if('${sessionScope.user.userNo}' === evt.target.dataset.userNo){
		  location.href = '${contextPath}/blog/detail.do?blogNo=' + evt.target.dataset.blogNo;
     } else {
    	location.href = '${contextPath}/blog/updateHit.do?blogNo=' + evt.target.dataset.blogNo;
     }	   
	  })
  }
  
  
  const fnInsertCount = () => {
	  let insertCount = '${insertCount}';
	  if(insertCount !== ''){
		  if(insertCount === '1'){
			  alert('블로그가 등록되었습니다.'); 
		  } else {
			  alert('블로그가 등록에 실패했습니다.');
		  }
	  }
  }
  
  fnInsertCount();
  fnGetBlogList();
  fnBlogDetail();
  fnScrollHandler();
</script>

 
 <%@include file="../layout/footer.jsp" %>