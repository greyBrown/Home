<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- include libraries(jquery, bootstrap) -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<!-- include summernote css/js -->
<link rel="stylesheet" href="${contextPath}/resources/summernote-0.8.18-dist/summernote.min.css">
<script src="${contextPath}/resources/summernote-0.8.18-dist/summernote.min.js"></script>
<script src="${contextPath}/resources/summernote-0.8.18-dist/lang/summernote-ko-KR.min.js"></script>
</head>
<body>
  
  <%-- 
  <script>
  $(document).ready(function(){  // 이부분은 자바스크립트를 이렇게 태그 위에 놓아도 작동할 수 있게 하는 제이쿼리 이벤트이다. 그렇다...
  $('#contents').summernote();
  })
  </script>
--%>

  <form method="POST"
        action="${contextPath}/board/register.do">
  <div>
    <textarea id="contents" name="contents"></textarea>
  </div>
  <div>
    <button type="submit">전송</button>
  </div>      
  </form>
            
   <script>
  $(document).ready(function(){  
  $('#contents').summernote({
	  width: 1024,
	  height: 500,
	  lang:'ko-KR',
	  callbacks: {
		  onImageUpload: (images)=>{
			  // 비동기 방식을 이용한 이미지 업로드 
			  for(let i = 0; i < images.length; i++){
			    let formData = new FormData();  // 폼 태그가 없어도 자바스크립트로 이렇게 폼 태그를 만들 수 있다.
				  formData.append('image',images[i]);
  			  fetch('${contextPath}/summernote/imageUpload.do', {
  				  method: 'POST',
  				  body: formData
  			  /* $.ajax({   ------ 만약 $.ajax 으로 한다면 이렇게. fetch 랑은 그냥 방식의 차이~ 편한 쪽으로 하세요. prj08에 잘 설명돼 있습죠
  				  type: 'POST',
  				  url: '${contextPath}/summernote/imageUpload.do',
  				  data: formData,
  				  contentType: false,
  				  processDataL false
  				*/  
  			  }).then(response=>response.json())
  			  .then(resData=>{
  				  $('#contents').summernote('insertImage', resData.src); //insertImage 라는 속성이 있다. summernote에서 지정해놓은 속성
  				  //이미지태그 통째로 db에 넘어갔다가 넘어온다. 그래서 그 태그 경로대로 이미지를 확인하는 형식
  				  //<p><img src="/prj10/upload/2024/03/27/9e571b3f436941a7a4505a3fb65716a4.png" style="width: 100px;">오늘의 학습: 이것저것 배웠다.<br></p>
  				  //contents에 이렇게 넘어옴.
  				  // 이렇게 텍스트주소를 넘기고 받는 이유 : DB에 진짜 이미지 파일 저장하면 용량이 어마어마하다.
  			  });
			  }
		  }		  
	  }
  });
  })
  </script>
        
</body>
</html>