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
<style>
.board {
  width: 200px;
  height: 100px;
  border: 1px solid gray;
  cursor: pointer;
}
</style>
</head>
<body>

  <div>
    <button type="button" id="btn-list">목록갱신</button>
  </div>
  
  <hr>
  
  <div id="board-list"></div>
  
   <script>
    
    const fnBoardList = ()=> {
        $('#btn-list').on('click', (evt)=>{
          $.ajax({
            /* 요청 */
            type: 'GET',
            url: '${contextPath}/ajax1/list.do',
            /* 응답 */
            dataType: 'json', 
            success: (resData)=>{
            	const boardList = $('#board-list');
            	boardList.empty();
                $.each(resData, (i, board)=>{
                	boardList.append('<div class="board"><div class="board-no">' + board.boardNo + '</div><div>' + board.title + '</div><div>' + board.contents + '</div></div>');
                })          
            }
          })          
        })
    }
    
    fnBoardList();
  
  </script>
  
  <script>
  
    var detailWindow = null;  // 너비/높이/top-left 
  
    const fnBoardDetail = ()=>{
    	$(document).on('click', '.board', (evt)=>{  //동적요소 이벤트 처리하기 (정적요소 이벤트 처리랑 약간 다름)
    	   // evt.target        : .board 내부 요소 중 실제로 클릭한 요소
    	   // evt.currentTarget : .board 자체
    		const boardNo = $(evt.currentTarget).find('.board-no').text();  // 프론트단에서 빽단으로 넘역주는 번호 구현
    	  $.ajax({
    		  /* 요청 */
    		  type: 'GET',
    		  url: '${contextPath}/ajax1/detail.do',           //ajax 은 이렇게 주소와 파라미터를 분리할 수 있음 (주소에서 파라미터 빠지고 data로 싹 넘어감)
    			data: 'boardNo=' + boardNo,	           //요청 파라미터
    			/* 응답 */
    			dataType: 'json',
    			success: (resData)=>{
    				if(detailWindow === null || detailWindow.closed){
    				detailWindow = window.open('','', 'width=300, height=200, top=100, left=100');
    				detailWindow.document.write('<div>' + resData.boardNo + '</div>');
    				detailWindow.document.write('<div>' + resData.title + '</div>');
    				detailWindow.document.write('<div>' + resData.contents + '</div>');
    				} else {
    					alert('먼저 기존 창을 닫으세요.');
    					detailWindow.focus();
    					detailWindow.close(); // 열려있으면 그냥 닫아버릴 수도 있고~~
    				}
    			}
    	  })
    	})
    }
    
    fnBoardDetail();
    
  
  </script>


</body>
</html>