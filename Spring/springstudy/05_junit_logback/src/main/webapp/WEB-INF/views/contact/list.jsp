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
    <a href="${contextPath}/contact/write.do">작성화면열기</a>  
  </div>

  <hr>
  
  <div>
    <table border="1">
        <thead>
          <tr>
            <td>순번</td>
            <td>이름</td>
            <td>연락처</td>
            <td>버튼</td>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${contactList}" var="contact" varStatus="vs">
            <tr>
                <td>${vs.count}</td>
                <td>${contact.name}</td>
                <td>${contact.mobile}</td>
                <td> 
<!-- 버튼에 각각 안넣고 버튼 부모에 넣어줘도 된다. 그럼 한군데에만 넣어주면 끝남. 상세한테만 넣어주고 삭제는 형제 데이터를 찾아쓰라고 할수도 있고...부모가 나은듯 -->
<!-- 하지만 이러면 부모한테 정보 있따고 알려줘야함. 안해줘서 오류났죠...? ㅠ 형제데이터를 찾아써라~ 부모데이터를 찾아써라~ 이런지시들... -->
                   <button type="button" class="btn-detail" data-contact-no="${contact.contactNo}">상세</button>
                    <button type="button" class="btn-remove" data-contact-no="${contact.contactNo}">삭제</button>          <!-- 이런 요소에서 id 안주게 조심! class로 줘야함. for문으로 고유 요소인 id가 중복이 되어버리니까 -->
                </td>
            </tr>
          </c:forEach>
          </tbody>
    </table>
  </div>
  
  
  <script>
  // 이렇게 배열에 이벤트 넣어주고 뭐 하고 동적요소가 머시기 할때는 제이쿼리가 훨씬 편하고 적합함
     $('.btn-detail').on('click', (evt)=>{
      const contactNo = evt.target.dataset.contactNo;
      location.href = '${contextPath}/contact/detail.do?contact-no=' + contactNo;
    })
     $('.btn-remove').on('click', (evt)=>{
      if(confirm('연락처를 삭제할까요?')) {        
        const contactNo = $(evt.target).prev().data('contactNo');
        location.href = '${contextPath}/contact/remove.do?contact-no=' + contactNo;   
      }
    })
  
  </script>


</body>
</html>