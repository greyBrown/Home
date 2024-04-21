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

<style>
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css');
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap');
* {
  font-family: "Noto Sans KR", sans-serif;
  font-weight: 400;
}
</style>

<!-- include summernote css/js -->
<link rel="stylesheet" href="${contextPath}/resources/summernote-0.8.18-dist/summernote.min.css">
<script src="${contextPath}/resources/summernote-0.8.18-dist/summernote.min.js"></script>
<script src="${contextPath}/resources/summernote-0.8.18-dist/lang/summernote-ko-KR.min.js"></script>
</head>
<body>

  <!--  원래 signup 에 있던거 화면분할하면서 여기저기 흩어지고 주석 사라지고 그래서 여기에 모아놓음 fetch 등등 공부할 거 많다 -->
  <!--  부트스트랩을 적용해봄. 부트스트랩은 클래스를 사용한다. -->
  
<div class="m-3">

  <h1>Sign Up</h1>
  
  <form method="POST"
        action="${contextPath}/user/signup.do"
        id="frm-signup">
  
    <div class="row mb-3">
      <label for="inp-email" class="col-sm-3 col-form-label">아이디</label>
      <div class="col-sm-6"><input type="text" id="inp-email" name="email" class="form-control" placeholder="example@example.com"></div>
      <div class="col-sm-3"><button type="button" id="btn-code" class="btn btn-primary">인증코드받기</button></div>
      <div class="col-sm-3"id="msg-email"></div>
    </div>
    <div class="row mb-3">
      <label for="inp-code" class="col-sm-3 col-form-label">인증코드</label>
      <div class="col-sm-6"><input type="text" id="inp-code" class="form-control" placeholder="인증코드입력" disabled></div>
      <div class="col-sm-3"><button type="button" id="btn-verify-code" class="btn btn-primary" disabled>인증하기</button></div>
    </div>
    
    <hr class="my-3">
  
    <div class="row mb-3">
      <label for="inp-pw" class="col-sm-3 col-form-label">비밀번호</label>
      <div class="col-sm-6"><input type="password" id="inp-pw" name="pw" class="form-control"></div>
      <div class="col-sm-3"></div>
      <div class="col-sm-9" id="msg-pw"></div>
    </div>
    <div class="row mb-3">
      <label for="inp-pw2" class="col-sm-3 col-form-label">비밀번호 확인</label>
      <div class="col-sm-6"><input type="password" id="inp-pw2" class="form-control"></div>
      <div class="col-sm-3"></div>
      <div class="col-sm-9" id="msg-pw2"></div>
    </div>
    
    <hr class="my-3">
    
    <div class="row mb-3">
      <label for="inp-name" class="col-sm-3 col-form-label">이름</label>
      <div class="col-sm-9"><input type="text" name="name" id="inp-name" class="form-control"></div>
      <div class="col-sm-3"></div>
      <div class="col-sm-9" id="msg-name"></div>
    </div>

    <div class="row mb-3">
      <label for="inp-mobile" class="col-sm-3 col-form-label">휴대전화번호</label>
      <div class="col-sm-9"><input type="text" name="mobile" id="inp-mobile" class="form-control"></div>
      <div class="col-sm-3"></div>
      <div class="col-sm-9" id="msg-mobile"></div>
    </div>

    <div class="row mb-3">
      <label class="col-sm-3 form-label">성별</label>
      <div class="col-sm-3">
        <input type="radio" name="gender" value="none" id="rdo-none" class="form-check-input" checked>
        <label class="form-check-label" for="rdo-none">선택안함</label>
      </div>
      <div class="col-sm-3">
        <input type="radio" name="gender" value="man" id="rdo-man" class="form-check-input">
        <label class="form-check-label" for="rdo-man">남자</label>
      </div>
      <div class="col-sm-3">
        <input type="radio" name="gender" value="woman" id="rdo-woman" class="form-check-input">
        <label class="form-check-label" for="rdo-woman">여자</label>
      </div>
    </div>
    
    <hr class="my-3">
    
    <div class="form-check mb-3">
      <input type="checkbox" class="form-check-input" id="chk-service">
      <label class="form-check-label" for="chk-service">서비스 이용약관 동의(필수)</label>
      <!-- 필수는 무조건 넘어가니까 name 뺌 -->
    </div>
    <div>
      <textarea rows="5" class="form-control">본 약관은 ...</textarea>
    </div>
    
    <div class="form-check mb-3">
      <input type="checkbox" name="event" class="form-check-input" id="chk-event">
      <label class="form-check-label" for="chk-event">
        이벤트 알림 동의(선택)
      </label>
    </div>
    <div>
      <textarea rows="5" class="form-control">본 약관은 ...</textarea>
    </div>
    
    <hr class="my-3">
    
    <div class="m-3">
      <button type="submit" id="btn-signup" class="btn btn-primary">가입하기</button>
    </div>
    
  </form>

</div>
  
 <script>
 
var emailCheck = false;
var passwordCheck = false;
var passwordConfirm = false;
var nameCheck = false;
var mobileCheck = false;
var agreeCheck = false;

const fnGetContextPath = ()=>{
  const host = location.host; 
  const url = location.href; 
  const begin = url.indexOf(host) + host.length;
  const end = url.indexOf('/', begin + 1);
  return url.substring(begin, end);
}
 <%-- 만약 ajax를 썻다면 이렇게 돌아돌아가야한다.(Promise 객체가 내장되어 있지 않기 때문)
 new Promise((resolve, reject) => {
$.ajax({
url: '이메일중복체크요청'
})
.done(resData => {
if(resData.enableEmail){
resolve();       
} else {
reject();}})})
.then( () => {   
  $.ajax({             
  url: '인증코드전송요청'
    })
.done(resData => {
   if(resData.code == 인증코드입력값
   })
})       
.catch(()=>{   // 실패
}) 
 --%>
 <%-- 이렇게 비동기처리가 연속적으로 필요할 때는 fetch를 쓰는 것이 훨씬훨씬 간단하다.  --%>
 
 
 /* 요약하자면 이런 구조로 전개된다
 fetch('이메일중복체크요청', {})
 .then(response=>response.json())
 .then(resData=>{
   if(resData.enableEmail){
     fetch('인증코드전송요청', {})
     .then(response=>response.json())
     .then(resData=>{  // {"code": "123asb"}
       if(resData.code === 인증코드입력값)
     })
   }
 })
 */
 
 <%-- 정규식을 통해 이메일 양식을 한번 걸러냄 -> DB로 가서 중복되는 아이디가 있는지 확인함 --%>
const fnCheckEmail = ()=>{
  let inpEmail = document.getElementById('inp-email');
    let regEmail = /^[A-Za-z0-9-_]{2,}@[A-Za-z0-9]+(\.[A-Za-z]{2,6}){1,2}$/;
    if(!regEmail.test(inpEmail.value)){
      alert('이메일 형식이 올바르지 않습니다.');
      emailCheck = false;
      return;
    }
    
  fetch(fnGetContextPath() + '/user/checkEmail.do', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ //자바스크립트 객체를 JSON으로 변환해주는 메소드
        'email': inpEmail.value
    })
  })
  .then(response => response.json()) // 받아온 응답 객체 데이터에서 json만 꺼내겠다. 이 json이 promise와 함께 오니까 다시 then.
  .then(resData => {                
    // 만약 $ajax로 처리했다면 new promise가 나오게 되는 예제이다. 비동기작업을 순차적으로 처리해야하기 때문에... 
    // 여기선 fetch가 좋은 선택임. promise가 내장되어 있기 때문에 따로 불러올 필요가 없다.
    if(resData.enableEmail){
      document.getElementById('msg-email').innerHTML = '';  // 아무것도 안했는데 사용가능한 이메일입니다 이런거 뜨는거 방지하려고 초기화.
      fetch(fnGetContextPath() + '/user/sendCode.do', {    // 이메일 중복체크 통과하면 이메일인증 시작하기. 이렇게 순서대로 진행해야 하기때문에 promise가 필요해 진다는 것.        
        method: 'POST',
          headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({ 
              'email':inpEmail.value
           })
      })
        .then(response => response.json())
          .then(resData => {  // resData = {"code": "123qaz"}
            alert(inpEmail.value + '로 인증코드를 전송했습니다.');
            let inpCode = document.getElementById('inp-code');
            let btnVerifyCode = document.getElementById('btn-verify-code');
            inpCode.disabled = false;
            btnVerifyCode.disabled = false;
            btnVerifyCode.addEventListener('click', (evt) => {
              if(resData.code === inpCode.value) {
                alert('인증되었습니다.');
                emailCheck = true;
              } else {
                alert('인증되지 않았습니다.');
                emailCheck = false;
              }
            })
          })
        } else {
          document.getElementById('msg-email').innerHTML = '이미 사용 중인 이메일입니다.';
          emailCheck = false;
          return;
        }
      })
    }
    

const fnCheckPassword = () => {
  // 비밀번호 4 ~ 12자, 영문/숫자/특수문자 중 2개 이상 포함
  // 영문, 숫자, 특수문자 검사 다 따로 할 것임. 함께 하는게 아니라!
  let inpPw = document.getElementById('inp-pw');
  let validCount = /[A-Za-z]/.test(inpPw.value)       // 영문 포함되어 있으면 true 를 반환  (JavaScript 에서 true 는 숫자 1과 같다.)
                 + /[0-9]/.test(inpPw.value)          // 숫자 포함되어 있으면 true
                 + /[^A-Za-z0-9]/.test(inpPw.value);   // 영문/숫자가 아니면 true
  
 let passwordLength = inpPw.value.length;
 passwordCheck = passwordLength >= 4
              && passwordLength <= 12
              && validCount >= 2;
 let msgPw = document.getElementById('msg-pw');             
 if(passwordCheck){
   msgPw.innerHTML = '사용 가능한 비밀번호입니다.';
 } else{
   msgPw.innerHTML = '비밀번호 4 ~ 12자, 영문/숫자/특수문자 중 2개 이상 포함';
 }
}

const fnConfirmPassword = () => {
  let inpPw = document.getElementById('inp-pw');
  let inpPw2 = document.getElementById('inp-pw2');
  passwordConfirm = (inpPw.value !== '') 
                 && (inpPw.value === inpPw2.value)
  let msgPw2 = document.getElementById('msg-pw2');
  if(passwordConfirm){
    msgPw2.innerHTML = '';
  } else{
    msgPw2.innerHTML = '비밀번호 입력을 확인하세요.';
  }
}

/* 아스키코드범위(0~127)에 포함되는 모든 문자는 한글자가 1Byte (이 범위에 한글 안들어옴)  => 아스키 범위에 들어오면 1Byte, 벗어나면 2Byte로 계산*/
const fnCheckName = () => {
  let inpName = document.getElementById('inp-name');
  let name = inpName.value;
  let totalByte = 0;
  for(let i = 0; i < name.length; i++){
    if(name.charCodeAt(i) > 127) { // 코드값이 127 초과이면 한 글자 당 2바이트 처리한다.
      totalByte += 2;
    } else {
      totalByte++;
    }
  }
  nameCheck = (totalByte <= 100);
  let msgName = document.getElementById('msg-name');
  if(!nameCheck){
    msgName.innerHTML = '이름은 100 바이트를 초과할 수 없습니다.';
  } else {
   msgName.innerHTML = '';
  }
}

const fnCheckMobile = () => {
  let inpMobile = document.getElementById('inp-mobile');
  let mobile = inpMobile.value;
  mobile = mobile.replaceAll(/[^0-9]/g, '');       // 숫자가 아닌것들,  숫자를 제외한 값들을 ''(빈문자열)로 바꾸겠다. [^] 제외 /^/ 시작 
  // Global-check 에러. 정규식 뒤에 저 g 붙이라는 뜻임
  mobileCheck = /^010[0-9]{7,8}$/.test(mobile);
  let msgMobile = document.getElementById('msg-mobile');
  if(mobileCheck){
    msgMobile.innerHTML = '';
  } else {
    msgMobile.innerHTML = '휴대전화를 확인하세요.';
  }
}

const fnCheckAgree = () =>  {
  let chkService = document.getElementById('chk-service');
  agreeCheck = chkService.checked;
}
     
const fnSignup = () => {
  document.getElementById('frm-signup').addEventListener('submit', (evt) => {
    fnCheckAgree();     // 필수약관동의는 따로 이벤트를 주는게 아니라 여기에 넣어줌.
      if(!emailCheck) {
        alert('이메일을 확인하세요.');
      evt.preventDefault();
      return; // submit 방지(preventDefault)와 함수 종료는 별도작업임. 각각 해주기! 이래야 이메일 체크가 안됐는데 가입되는 걸 방지한다.
    } else if(!passwordCheck || !passwordConfirm){       // 비밀번호 둘 중 하나로 통과 못하면 못하게 가입 못하게 막음
       alert('비밀번호를 확인하세요.');
       evt.preventDefault();
       return;
    } else if(!nameCheck){
      alert('이름을 확인하세요.');
      evt.preventDefault();
      return;
    } else if(!mobileCheck){
      alert('휴대전화번호를 확인하세요.');
      evt.preventDefault();
      return;
    } else if(!agreeCheck) {
      alert('서비스 약관에 동의해야 서비스를 이용할 수 있습니다.');
      evt.preventDefault();
      return;
      }
  })
}

document.getElementById('btn-code').addEventListener('click', fnCheckEmail);
document.getElementById('inp-pw').addEventListener('keyup', fnCheckPassword);
document.getElementById('inp-pw2').addEventListener('blur', fnConfirmPassword);
document.getElementById('inp-name').addEventListener('blur', fnCheckName);
document.getElementById('inp-mobile').addEventListener('blur', fnCheckMobile);

// 'keyup'(한글자씩), 'blur(다찍고확인)' 키보드 이벤트
fnSignup(); // 이제 이메일 인증을 안받고 가입하기를 누르면 '이메일을 확인하세요'가 뜬다

// POST 방식으로 email을 JSON 데이터로 만들어 controller로 보낼 것임
// -> 받는 쪽에서는 @RequestBody로 받는다. 받을 때 쓸 도구는 MAP이다. (Jackson이 상호 맵핑을 돕는다 (JSON <-> MAP)) 

// 한번에 받았다!!! ㅠㅠㅠ (개발할때는 인증코드 콘솔창에 뜨게 할 것! 그냥 그거 가져다 쓰는게 훨 나음)
 
 
 
 
 
 
 
 
 
 
 </script> 
  
</body>
</html>