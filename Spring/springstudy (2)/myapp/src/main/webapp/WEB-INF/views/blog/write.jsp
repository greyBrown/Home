<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="dt" value="<%=System.currentTimeMillis()%>"/>


<jsp:include page="../layout/header.jsp"/>

<!-- 버전이 뭔가 좀 달라져서...이렇게 아래에 넣어주면 덮어쓰기 됩니다. 다른 페이지에서는 여전히 header에 있는 상위버전을 쓰게 된다. -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

 <h1 class="title">블로그 작성화면</h1>
 
 <form id="frm-blog-register"
       method="POST"
       action = "${contextPath}/blog/register.do">
 <!-- 삽입할 땐 POST! 고민하지 않기 ㅎㅎ -->      
       
 <div>
  <span>작성자</span>
  <span>${sessionScope.user.email}</span>
 </div>      
 
  <div>
    <label for="title">제목</label>
    <input type="text" name="title" id="title">
  </div>
 
 <div>
   <textarea id="contents" name="contents" placeholder="내용을 입력하세요."></textarea>
 </div>
 
 <div>
 <input type="hidden" name="userNo" value="${sessionScope.user.userNo}">
 <button type="submit">작성완료</button>
 <a href="${contextPath}/blog/list.page"><button type="button">작성취소</button></a>
 </div>
 
 </form>
 
 <script>
 
 const fnSummernoteEditor = () => {
	    $('#contents').summernote({
	      width: 1024,
	      height: 500,
	      lang: 'ko-KR',
	      callbacks: {
	        onImageUpload: (images)=>{
	          // 비동기 방식을 이용한 이미지 업로드
	          for(let i = 0; i < images.length; i++) {
	            let formData = new FormData();  // 자바코드로 처리하기 위한 new FormData();
	            formData.append('image', images[i]); // <input type="file" name="image"> 와 같은 의미이다. 요걸 @RequestParam으로 받으면 된다. file type의 input을 multipartfile로 받는다.
	            fetch('${contextPath}/blog/summernote/imageUpload.do', {  // blog 컨트롤러로 주기위해 /blog requestMapping blog 로 했음~~
	              method: 'POST',
	              body: formData
	              /*  submit 상황에서는 <form enctype="multipart/form-data"> 필요하지만 fetch 에서는 사용하면 안 된다. 
	              headers: {
	                'Content-Type': 'multipart/form-data'
	              }
	              */
	            })
	            .then(response=>response.json())  //ResponseEntity가 반환해주는 json 데이터. ajax를 쓴다는건 JSON 데이터가 오고간다는거고(아님 뭐 xml이나..) JSON을 주고 받기 위해 MAP 형태로 주고받는 ResponseEntity 가 필요함
	            .then(resData=>{
	              $('#contents').summernote('insertImage', '${contextPath}' + resData.src); //summernote가 최종적으로 만들어주는 건 Map.of("src", "image url") -> <img src="image url"> 요 태그임.
	            })
	          }
	        }
	      }
	    })
	  }
 
  const fnRegisterBlog = (evt) => {
	    if(document.getElementById('title').value === '') {
	      alert('제목 입력은 필수입니다.');
	      evt.preventDefault();
	      return;
	    }
	  }
  
  document.getElementById('frm-blog-register').addEventListener('submit', (evt) => {
	    fnRegisterBlog(evt);
	  })
	  
	  fnSummernoteEditor();
 
 </script>
 

 <%@include file="../layout/footer.jsp" %>