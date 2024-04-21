<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
</head>
<body>

  <div>
    <form action="${contextPath}/upload1.do"
          method="POST"
          enctype="multipart/form-data">
          <!-- multipart 요청의 담당은 스프링의 multipartResolver 가 담당하기로 약속. 이게 없으면 오류가 난다. 
          빈으로 만들고 등록해줘야 함. 스프링이 가져다 쓸 수 있도록!  -->
      <div>
      <!-- accept 이라는 속성을 추가해서 contentType을 명시할 수 있다. 이미지파일만 올릴 수 있게 설정. 아예 탐색기에서 안보임  -->
        <input type="file" name="files" class="files" accept="image/*" multiple>
      </div>
      <div>
        <input type="text" name="writer" placeholder="작성자">
      </div>
      <div>
        <button type="submit">전송</button>
      </div>      
    </form>  
  </div>
  
  <!-- 다중 첨부하면 파일 이름이 안보이게 되서 별도의 공간을 만들기로 함 -->
  <h3>첨부 파일 목록</h3>
  <div id="file-list"></div>
  
  
  
  <hr>
  
  <div>
  <!-- new FormData() 를 이용하면 form을 자바스크립트 객체로 만들 수 있다. -->
   <div>
      <input type="file" id="input-files" class="files" multiple>
   </div>
   <div>
      <input tyoe="text" id="input-writer" placeholder="작성자">
   </div>
   <div>
      <button type="button" id="btn-upload">전송</button>
   </div>
  </div>
  
  
  <script type="text/javascript">
  
  const fnFileCheck = ()=>{ 
	 $('.files').on('change', (evt)=>{                    // 첨부가 바뀌는 상황을 'change' 라고 본다.  
		 const limitPerSize = 1024 * 1024 * 10;             //10MB를 의미. 파일당 크기제한이 10MB
		 const limitTotalSize = 1024 * 1024 *100;           //100MB를 의미. 파일들 총 크기제한이 100MB
		 let totalSize = 0;
		 const files = evt.target.files;
		 const fileList = document.getElementById('file-list');           // 파일목록을 출력해주는 부분
		 fileList.innerHTML = '';
    //properties에서 확인해보면 filelist와 그 length가 뜬다. 즉 배열로 사용하게 된다.
		 for(let i = 0; i < files.length; i++){
			  if(files[i].size > limitPerSize){
				  alert('각 첨부 파일의 최대 크기는 10MB입니다.');
				  evt.target.value = '';   // value를 빈 문자열로 초기화. 즉 파일 빼버림
				  fileList.innerHTML = '';
				  return;                  // 그리고 함수 종료
			  }
			  totalSize += files[i].size;
			  if(totalSize > limitTotalSize){
				  alert('전체 첨부 파일의 최대 크기는 100MB입니다.');
				  evt.target.value = '';
				  fileList.innerHTML = '';
				  return;
			  }
			  fileList.innerHTML += '<div>' + files[i].name + '</div>';
		 }
	 })                        
  }
  
  const fnAfterInsertCheck = ()=>{
      const insertCount = '${insertCount}';   // attribute 은 el로 받을 수 있다. 문자열로 받아야 값이 전달되지 않더라도 오류가 발생하지 않음
      if(insertCount !== ''){
        if(insertCount === '1'){
          alert('저장되었습니다.');
        } else {
          alert('저장실패했습니다.');
        }
      }
    }
  
  const fnAsyncUpload = ()=>{  // 비동기처리방식
      const inputFiles = document.getElementById('input-files');
      const inputWriter = document.getElementById('input-writer');
      let formData = new FormData();
      for(let i = 0; i < inputFiles.files.length; i++){
        formData.append('files', inputFiles.files[i]);          //첨부된 '개별'파일 자체
      }
      formData.append('writer', inputWriter);
      fetch('${contextPath}/upload2.do', {
        method: 'POST',
        body: formData
      }).then(response=>response.json())
        .then(resData=>{  /* resData = {"success": 1} 또는 {"success": 0} */
          if(resData.success === 1){
            alert('저장되었습니다.');
          } else {
            alert('저장실패했습니다.');
          }
        })
    }
    
    // upload1과 좀 다른 방법으로 처리한다면? 방식 $.ajax 사용 (fetch 와 똑같음. 방식의 차이)
    const fnAsyncUpload2 = ()=>{
      const inputFiles = document.getElementById('input-files');
      const inputWriter = document.getElementById('input-writer');
      let formData = new FormData();
      for(let i = 0; i < inputFiles.files.length; i++){
        formData.append('files', inputFiles.files[i]);
      }
      formData.append('writer', inputWriter);
      $.ajax({
        type: 'POST',
        url: '${contextPath}/upload2.do',
        contentType: false,  // content-type header를 생성하지 않도록 설정
        data: formData,      // FormData 객체를 서버로 전달
        processData: false,  // 전달되는 데이터가 객체인 경우 객체를 {property: value} 형식의 문자열로 자동으로 변환해서 전달하는데 이를 방지하는 옵션
        dataType: 'json'
      }).done(resData=>{
        if(resData.success === 1){
          alert('저장되었습니다.');
        } else {
          alert('저장실패했습니다.');
        }
      })
    }
  
    fnFileCheck();
    fnAfterInsertCheck();
    document.getElementById('btn-upload').addEventListener('click', fnAsyncUpload2);
  
  
  
  
  </script>
  


</body>
</html>