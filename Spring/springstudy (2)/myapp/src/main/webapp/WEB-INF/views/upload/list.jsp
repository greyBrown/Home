<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="dt" value="<%=System.currentTimeMillis()%>"/>


<jsp:include page="../layout/header.jsp"/>
 


 <h1 class="title">업로드 목록</h1>
 
<a href="${contextPath}/upload/write.page">업로드작성</a>

<div> 
<input type="radio" name="sort" value="DESC" id="descending" checked>
<label for="descending">내림차순</label>
<input type="radio" name="sort" value="ASC" id="ascending">
<label for="ascending">오름차순</label>
</div>


  <div>
    <select id="display" name="display">
      <option>20</option>
      <option>30</option>
      <option>40</option>
    </select>
  </div>

<div>
  <table class="table align-middle">
    <thead>
     <tr>
       <td>순번</td>
       <!-- 이런 순번에 ${uploadNo} 이런거 쓰면 최악이라고 누누이 강조했음. 중간에 몇개 삭제되면 뭐야뭐야 왜이래 된다 -->
       <td>제목</td>
       <td>작성자</td>
       <td>첨부개수</td>
     </tr>
    </thead>
    <tbody>
      <c:forEach items="${uploadList}" var="upload" varStatus="vs">
        <tr>
          <td>${beginNo - vs.index}</td>        
          <td>   
          <a href="${contextPath}/upload/detail.do?uploadNo=${upload.uploadNo}">${upload.title}</a></td>        
          <td>${upload.user.email}</td>        
          <td>${upload.attachCount}</td>        
        </tr>
      </c:forEach>
    </tbody>
    <tfoot>
      <tr>
        <td colspan="4">${paging}</td>
      </tr>
    </tfoot>  
  </table>
</div>

<script>
		// display 변경하면 새롭게 페이지를 요청한다. 새롭게 변경된 페이지는 display가 반영된 1page가 나오게 한다.
		// display 는 지금 evt.target 이니까 그 값을 가져온다.
const fnDisplay = () => {
  document.getElementById('display').value = '${display}';
  document.getElementById('display').addEventListener('change', (evt) => {
    location.href = '${contextPath}/upload/list.do?page=1&sort=${sort}&display=' + evt.target.value;
  })
}

const fnSort = () => { // 처음 게 안된이유. radio 가 2개 이상이다 보니 querySelectorAll로 js가 가져옴. 대상이 여러개니까 제이쿼리쓰지 않고 이벤트 걸려면 for문 써야함.
	                     // jQuery로 이벤트를 걸어줘야 for문을 안돌리고 깔끔하게 뽑음 :radio 이거 자체가 jQuery 문법이기도 하고!
	  $(':radio[value=${sort}]').prop('checked', true);
    $(':radio').on('click', (evt) => {
    location.href = '${contextPath}/upload/list.do?page=${page}&sort=' + evt.target.value + '&display=${display}';
	})
}

const fnUploadInserted = () => {
	const inserted = '${inserted}';
	if(inserted !== ''){
		if(inserted === 'true') {
			alert('업로드 되었습니다.')
		} else {
			alert('업로드가 실패했습니다.');
		}
	}
}




fnDisplay();
fnSort();
fnUploadInserted();



</script>

 
 <%@include file="../layout/footer.jsp" %>