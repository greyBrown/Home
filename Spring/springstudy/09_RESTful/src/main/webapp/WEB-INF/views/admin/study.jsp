<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<script>
/**
 * 
 */
 
// 전역변수
var page = 1;
var display = 20;
// jQuery 객체 선언
var members = $('#members');
var total = $('#total');
var jqDisplay = $('#display');
var paging = $('#paging');
var email = $('#email');
var mName = $('#name');
// var gender = $('input[type=radio][name=gender]'); // :radio[name:gender] 로 줄여서 표현가능
var zonecode = $('#zonecode');
var address = $('#address');
var detailAddress = $('#detailAddress');
var extraAddress = $('#extraAddress');
var btnInit = $('#btn-init');
var btnRegister = $('#btn-register');
var btnModify = $('#btn-modify');
var btnRemove = $('#btn-remove');
var btnSelectRemove = $('#btn-select-remove');


// 함수 표현식 (함수 만들기)
const fnInit = ()=>{
  email.val('');
  mName.val('');
  $('#none').prop('checked', true);
 //$('#none').attr('checked', 'checked'); 이렇게도 표현할 수 있음. property를 사용하냐 attribute를 사용하냐 무슨 형태를 사용하냐에 따라 나뉜다.
  zonecode.val('');
  address.val('');
  detailAddress.val('');
  extraAddress.val('');
}

const getContextPath = ()=>{
  const host = location.host; /* localhost:8080 */
  const url = location.href   /* http://localhost:8080/mvc/getDate.do */
  const begin = url.indexOf(host) + host.length;
  const end = url.indexOf('/', begin + 1);
  return url.substring(begin, end);
}

const fnRegisterMember = ()=>{
  $.ajax({
    // 요청
    type: 'POST',
    url: getContextPath() + '/members',
    contentType: 'application/json',  // 보내는 데이터의 타입
    data: JSON.stringify({            // 보내는 데이터 (문자열 형식의 JSON 데이터)
      'email': email.val(),
      'name': mName.val(),
      'gender': $(':radio:checked').val(),
      'zonecode': zonecode.val(),
      'address': address.val(),
      'detailAddress': detailAddress.val(),
      'extraAddress': extraAddress.val()
    }),
    // 응답
    dataType: 'json'  // 받는 데이터 타입
  }).done(resData=>{  // resData = {"insertCount": 2}
    if(resData.insertCount === 2){
      alert('정상적으로 등록되었습니다.');
      fnInit();
    }
  }).fail(jqXHR=>{
    alert(jqXHR.responseText);
  })
}

const fnMemberList = ()=>{
    $.ajax({
      type: 'GET',
      url: getContextPath() + '/members/page/' + page + '/display/' + display,
      dataType: 'json', 
                  success: (resData)=>{   /*
                          resData = {
                          "members": [
                            {
                              "addressNo": 1,
                              "zonecode": '12345',
                              "address": '서울시 구로구'
                              "detailAddress": '디지털로',
                              "extraAddress": '(가산동)',
                              "member": {
                                "memberNo": 1,
                                "email": 'aaa@bbb',
                                "name": 'gildong',
                                "gender": 'none'
                              }
                            }, ...
                          ],
                          "total": 30,
                          "paging": '< 1 2 3 4 5 6 7 8 9 10 >'
                        }
                     */
      total.html('총 회원' + resData.total + '명');
        members.empty();
        $.each(resData.members, (i, member)=>{
          let str = '<tr>';
          str += '<td><input type="checkbox" class="chk-member" value="'+ member.member.memberNo +'"></td>';
          str += '<td>' + member.member.email + '</td>';
          str += '<td>' + member.member.name + '</td>';
          str += '<td>' + member.member.gender + '</td>';
          str += '<td><button type="button" class="btn-detail" data-member-no="'+ member.member.memberNo +'">조회</button></td>';
          str += '</tr>';
          members.append(str);
        })
        paging.html(resData.paging);
      },
      error: (jqXHR)=>{
        alert(jqXHR.statusText + '(' + jqXHR.status + ')');
      }
        // success/error(스코프 안에 작성) 로 할건지 done/fail(스코프 밖에 작성) 로 할 것인지는 항상 말했듯 본인 선택임! 어차피 들어가는건 똑같음
    })
  }
  
  //MyPageUtils 클래스의 getAsyncPaging() 메소드에서 만든 <a href="javascript:fnPaging()"> 에 의해서 실행되는 함수
  const fnPaging = (p)=>{
    page = p;
    fnMemberList();
  }     // 이 파트 이해 못함...짧은데...아니 짧아서.....ㅎ.......공부해보자...
  
  const fnChangeDisplay = ()=>{
  display = jqDisplay.val();
  fnMemberList();
}

// 함수 호출 및 이벤트
fnInit();
btnInit.on('click', fnInit);
btnRegister.on('click', fnRegisterMember);
fnMemberList();
jqDisplay.on('change', fnChangeDisplay);

//jQuery 객체 선언
var btnDetail = $('.btn-detail');

 // 함수 표현식 (함수 만들기)
 const getMemberByNo = (evt)=>{
   $.ajax({
     type: 'GET',
     url: getContextPath() + '/members/' + evt.target.dataset.memberNo,
     dataType: 'json'
   }).done(resData=>{  /* resData =  {
                               "addressList": [
                                 {
                                   "addressNo":1,
                                   "zonecode": "12345",
                                   "address":"서울시 구로구 디지털로",
                                   "detailAddress": "카카오", 
                                   "extraAddress":"(가산동)"
                                 },
                                ...  
                               ],
                               "member": {
                                 "memberNo":1,
                                 "email": "email@email.com",
                                 "name":"gildong",
                                 "gender": "man"
                                  }
                                }
                         */
     email.val(resData.member.email);
     mName.val(resData.member.name);                                           //radio는 input 처럼 val()로 세팅해줄 수 가 없어 다르게 접근해야 함.
     $(':radio[value=' + resData.member.gender + ']').prop('checked', true);  // radio 중에 value가 man 이나 woman인 걸 구하고 전달된 데이터 중 해당하는 것을 체크해라.
     zonecode.val(resData.addressList[0].zonecode);
     address.val(resData.addressList[0].address);
     detailAddress.val(resData.addressList[0].detailAddress);
     extraAddress.val(resData.addressList[0].extraAddress);
   }).fail(jqXHR=>{
     alert(jqXHR.statusText + '(' + jqXHR.status + ')');
   })
 }
 
 


 // 함수 호출 및 이벤트
 $(document).on('click', btnDetail, (evt)=>{ getMemberByNo(evt); })
 
 
 
 
 /* const getMemberByNo = (evt)=>{ 
    $.ajax({
      type: 'GET',
      url: getContextPath() + '/members/' + evt.target.dataset.memberNo,
      dataType: 'json'
    }).done(resData=>{  
      email.val(resData.member.email);
      mName.val(resData.member.name);                                           //radio는 input 처럼 val()로 세팅해줄 수 가 없어 다르게 접근해야 함.
      $(':radio[value=' + resData.member.gender + ']').prop('checked', true);  // radio 중에 value가 man 이나 woman인 걸 구하고 전달된 데이터 중 해당하는 것을 체크해라.
      zonecode.val(resData.addressList[0].zonecode);
      address.val(resData.addressList[0].address);
      detailAddress.val(resData.addressList[0].detailAddress);
      extraAddress.val(resData.addressList[0].extraAddress);
    }).fail(jqXHR=>{
      alert(jqXHR.statusText + '(' + jqXHR.status + ')');
    })
  }
  이 부분이 조회할때 비어있는 정보가 있어서 계속 error가 떴던(서버는 계속 멀쩡히 작동되지만 조회 누를때마다 뜬 그거) 코드
  강사님이 수정하신후 해당 오류는 더 이상 뜨지 않음. 어떻게 안뜨게 만드신건지 먼저거(이거) 고치신거(js) 보면서 봐보기
 */ 
 
 
 const fnRemoveMembers = ()=>{
	  // 체크된 요소를 배열에 저장하기
	  let arr = [];
	  $.each($('.chk-member'), (i, chk)=>{
	    if($(chk).is(':checked')){
	      arr.push(chk.value);
	    }
	  })
	  // 체크된 요소가 없으면 함수 종료
	  if(arr.length === 0){
	    alert('선택된 회원 정보가 없습니다.');
	    return;
	  }
	  // 삭제 확인
	  if(!confirm('선택된 회원 정보를 모두 삭제할까요?')){
	    return;
	  }
	  // 삭제
	  $.ajax({
	    type: 'DELETE',
	    url: fnGetContextPath() + '/members/' + arr.join(','),
	    dataType: 'json',
	    success: (resData)=>{  // {"deleteCount": 3}
	      if(resData.deleteCount === arr.length){
	        alert('선택된 회원 정보가 삭제되었습니다.');
	        vPage = 1;
	        fnGetMemberList();
	      } else {
	        alert('선택된 회원 정보가 삭제되지 않았습니다.');
	      }
	    },
	    error: (jqXHR)=>{
	      alert(jqXHR.statusText + '(' + jqXHR.status + ')');
	    }
	  })
	}

  
</script>
</body>
</html>