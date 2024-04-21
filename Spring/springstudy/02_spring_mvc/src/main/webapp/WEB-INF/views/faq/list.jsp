<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>faq 목록</title>
<script>
  function fnAddResult(){
    let addResult = '${addResult}';                    //  '' 를 붙여주는 이유. 다른 경로로 돌아와서 addResult가 세팅되지 않으면 값이 비면서 null 이 되게 됨. '' 처리로 빈 문자열을 만든다.
    if(addResult !== '' && addResult === '1'){ 
      alert('정상적으로 등록되었습니다.');
    }
  }
  fnAddResult();
</script>
</head>
<body>

</body>
</html>