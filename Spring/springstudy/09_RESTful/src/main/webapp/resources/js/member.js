/*************************************************
 * 파일명 : member.js
 * 설  명 : 회원 관리 JavaScript
 * 수정일      수정자  Version   Function 명
 * -------------------------------------------
 * 2024.03.25  민경태  1.0       fnInit
 * 2024.03.25  민경태  1.1       fnRegisterMember
 * 2023.03.26  민경태  1.2       fnGetContextPath
 *************************************************/

// 전역변수 (vXXX)
var vPage = 1;
var vDisplay = 20;

// jQuery 객체 선언 (jqXXX)
var jqMembers = $('#members');
var jqTotal = $('#total');
var jqPaging = $('#paging');
var jqDisplay = $('#display');
var jqEmail = $('#email');
var jqName = $('#name');
var jqZonecode = $('#zonecode');
var jqAddress = $('#address');
var jqDetailAddress = $('#detailAddress');
var jqExtraAddress = $('#extraAddress');
var jqBtnInit = $('#btn-init');
var jqBtnRegister = $('#btn-register');
var jqBtnModify = $('#btn-modify');
var jqBtnRemove = $('#btn-remove');
var jqBtnSelectRemove = $('#btn-select-remove');
var jqMemberNo = $('#member-no');

/*************************************************
 * 함수명 : fnInit
 * 설  명 : 입력란에 입력된 데이터를 모두 초기화
 * 인  자 : 없음
 * 사용법 : fnInit()
 * 작성일 : 2024.03.26
 * 작성자 : 이런저런 개발팀 민경태
 * 수정일     수정자  수정내용
 * --------------------------------
 * 2024.03.25 민경태  입력란 초기화
 *************************************************/
const fnInit = ()=>{
  jqEmail.val('').prop('disabled', false); // 여긴 등록중이니까 등록이 가능하게 disabled 해지해줘야함
  jqName.val('');
  $('#none').prop('checked', true);
  jqZonecode.val('');
  jqAddress.val('');
  jqDetailAddress.val('');
  jqExtraAddress.val('');
  jqBtnRegister.prop('disabled', false); // 등록버튼만 활성화
  jqBtnModify.prop('disabled', true);  // 수정, 삭제버튼은 여기서 조작하지 못하게 막음
  jqBtnRemove.prop('disabled', true);
  
}

const fnGetContextPath = ()=>{
  const host = location.host;  /* localhost:8080 */
  const url = location.href;   /* http://localhost:8080/mvc/getDate.do */
  const begin = url.indexOf(host) + host.length;
  const end = url.indexOf('/', begin + 1);
  return url.substring(begin, end);
}

const fnRegisterMember = ()=>{
  $.ajax({
    // 요청
    type: 'POST',
    url: fnGetContextPath() + '/members',
    contentType: 'application/json',  // 보내는 데이터의 타입
    data: JSON.stringify({            // 보내는 데이터 (문자열 형식의 JSON 데이터)
      'email': jqEmail.val(),
      'name': jqName.val(),
      'gender': $(':radio:checked').val(),
      'zonecode': jqZonecode.val(),
      'address': jqAddress.val(),
      'detailAddress': jqDetailAddress.val(),
      'extraAddress': jqExtraAddress.val()
    }),
    // 응답
    dataType: 'json'  // 받는 데이터 타입
  }).done(resData=>{  // resData = {"insertCount": 2}
    if(resData.insertCount === 2){
      alert('정상적으로 등록되었습니다.');
      fnInit();
      fnGetMemberList();
    }
  }).fail(jqXHR=>{
    alert(jqXHR.responseText);
  })
}

const fnGetMemberList = ()=>{
  $.ajax({
    type: 'GET',
    url: fnGetContextPath() + '/members/page/' + vPage + '/display/' + vDisplay,
    dataType: 'json',
    success: (resData)=>{  /*
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
      jqTotal.html('총 회원 ' + resData.total + '명');
      jqMembers.empty();
      $.each(resData.members, (i, member)=>{
        let str = '<tr>';
        str += '<td><input type="checkbox" class="chk-member" value="' + member.member.memberNo + '"></td>';
        str += '<td>' + member.member.email + '</td>';
        str += '<td>' + member.member.name + '</td>';
        str += '<td>' + member.member.gender + '</td>';
        str += '<td><button type="button" class="btn-detail" data-member-no="' + member.member.memberNo + '">조회</button></td>';
        str += '</tr>';
        jqMembers.append(str);
      })
      jqPaging.html(resData.paging);
    },
    error: (jqXHR)=>{
      alert(jqXHR.statusText + '(' + jqXHR.status + ')');
    }
  })
}

// MyPageUtils 클래스의 getAsyncPaging() 메소드에서 만든 <a href="javascript:fnPaging()"> 에 의해서 실행되는 함수
const fnPaging = (p)=>{
  vPage = p;
  fnGetMemberList();
}

const fnChangeDisplay = ()=>{
  vDisplay = jqDisplay.val();
  fnGetMemberList();
}

const fnGetMemberByNo = (evt)=>{
  $.ajax({
    type: 'GET',
    url: fnGetContextPath() + '/members/' + evt.target.dataset.memberNo,
    dataType: 'json'
  }).done(resData=>{  /* resData = {
                           "addressList": [
                             {
                               "addressNo": 1,
                               "zonecode": "12345",
                               "address": "서울시 구로구 디지털로",
                               "detailAddress": "카카오",
                               "extraAddress": "(가산동)"
                             },
                             ...
                           ],
                           "member": {
                             "memberNo": 1,
                             "email": "email@email.com",
                             "name": "gildong",
                             "gender": "man"
                           }
                         }
                      */
    fnInit();
    if(resData.member !== null){
      jqMemberNo.val(resData.member.memberNo);
      jqEmail.val(resData.member.email).prop('disabled', true); // 조작하지 못하게 막기. readonlt, disabled 중 disabled 방법으로.(prop도 가능 attr도 가능)
      jqName.val(resData.member.name);
      $(':radio[value=' + resData.member.gender + ']').prop('checked', true);
      jqBtnRegister.prop('disabled', true); // 등록버튼 막음
      jqBtnModify.prop('disabled', false);  // 수정, 삭제 버튼 활성화
      jqBtnRemove.prop('disabled', false);
    }
    if(resData.addressList.length !== 0){
      jqZonecode.val(resData.addressList[0].zonecode);
      jqAddress.val(resData.addressList[0].address);
      jqDetailAddress.val(resData.addressList[0].detailAddress);
      jqExtraAddress.val(resData.addressList[0].extraAddress);
    }
  }).fail(jqXHR=>{
    alert(jqXHR.statusText + '(' + jqXHR.status + ')');
  })
}
const fnModifyMember = ()=>{
  $.ajax({
    type: 'PUT',
    url: fnGetContextPath() + '/members', 
    contentType: 'application/json',
    data: JSON.stringify({ 
      // 맵퍼에서 작성한 변수명 들이 여기서 만드는 객체의 프로퍼티 이름. 샵 중괄호 그거...아 주석을 뚫고 에러를 만드네...? 
      'memberNo': jqMemberNo.val(),
      'name': jqName.val(),
      'gender': $(':radio:checked').val(),
      'zonecode': jqZonecode.val(),
      'address': jqAddress.val(),
      'detailAddress': jqDetailAddress.val(),
      'extraAddress': jqExtraAddress.val()
    }),
    dataType: 'json', // 받아오는 데이터의 타입
    success: (resData)=>{ // resData = {"updateCount" : 2} <- 성공일 시 기대하는 값
      if(resData.updateCount === 2) {
        alert('회원 정보가 수정되었습니다.');
        fnGetMemberList();
      } else {
        alert('회원 정보가 수정되지 않았습니다.'); // 이름 수정은 멀쩡히 되는데 ADDRESS_T에 더미만 넣어놔서.... alert창에 안된다고 뜸 ㅎ
      }
    },
    error: (jqXHR)=>{
      alert(jqXHR.statusText + '(' + jqXHR.status + ')');
    }
  })
}

const fnRemoveMembers = ()=>{
  // 체크된 요소를 배열에 저장하기
  let arr = [];
  $.each($('.chk-member'), (i, chk)=>{  // 하나씩 빼오는 chk 는 제이쿼리 객체가 아니므로 다시 래퍼로 감싸준다.
    if($(chk).is(':checked')){
      arr.push(chk.value);             // arr 에 저장하는 메소드 push. chk의 value를 배열에 저장하겠다.
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
    url: fnGetContextPath() + '/members/' + arr.join(','),    // 배열에서 쭈르륵 꺼내주는 메소드 join
    dataType: 'json',
    success: (resData)=>{   // {"deleteCount": 3}
      if(resData.deleteCount === arr.length){
        alert('선택된 회원 정보가 삭제되었습니다.');
        vPage = 1;
        fnGetMemberList();
      } else{
        alert('선택된 회원 정보가 삭제되지 않았습니다.');
      }
    },
    error: (jqXHR)=>{
        alert(jqXHR.statusText + '(' + jqXHR.status + ')');
     }
    })
  }
  
  const fnRemoveMember = ()=>{
  if(!confirm('삭제할까요?')){
    return;
  }
  $.ajax({
    type:'DELETE',
    url: fnGetContextPath() + '/member/' + jqMemberNo.val(),
    dataType: 'json'
  }).done(resData=>{ // {"deleteCount": 1}
    if(resData.deleteCount === 1){
      alert('회원 정보가 삭제되었습니다.');
      fnInit();
      vPage = 1;                                    // 마지막페이지에서 회원이 삭제되었을때 목록에 아무런 페이지가 안뜨는 사태를 방지
      fnGetMemberList();
    }else{
      alert('회원 정보가 삭제되지 않았습니다');
    }
  }).fail(jqXHR=>{
      alert(jqXHR.statusText + '(' + jqXHR.status + ')');
  })
}
  


// 함수 호출 및 이벤트
fnInit();
jqBtnInit.on('click', fnInit);
jqBtnRegister.on('click', fnRegisterMember);
fnGetMemberList();
jqDisplay.on('change', fnChangeDisplay);
$(document).on('click', '.btn-detail', (evt)=>{ fnGetMemberByNo(evt); });
jqBtnModify.on('click', fnModifyMember);
jqBtnRemove.on('click', fnRemoveMember);
jqBtnSelectRemove.on('click', fnRemoveMembers);
