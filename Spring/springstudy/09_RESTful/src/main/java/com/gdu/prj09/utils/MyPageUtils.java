package com.gdu.prj09.utils;

import lombok.Data;

@Data
public class MyPageUtils {
  
  private int total;             // 전체 멤버의 개수 from db
  private int display;           // 한 화면에 몇 개 표시할 거냐 (파라미터)
  private int page;              // 마찬가지로 파라미터 (이 위 3개를 서비스에서 받아와서 아래 2개 재료를 Map으로 만들어 목록을 list 처리를 해준다)
  private int begin;             // 위 3가지로 구함 
  private int end;               // 위 3가지로 구함
  
  private int pagePerBlock = 10;
  private int totalPage;
  private int beginPage;
  private int endPage;
  
  public void setPaging(int total, int display, int page) {
    
    this.total = total;
    this.display = display;
    this.page = page;
    
    begin = (page - 1) * display + 1;
    end = begin + display - 1;

    totalPage = (int) Math.ceil((double)total / display); // 올림처리 해줘야 한다는 거~ 기억나죵 21/5 해서 4 나오면 게시글 1개 증발함 
    beginPage = ((page - 1) / pagePerBlock) * pagePerBlock + 1 ;
    endPage = Math.min(totalPage, beginPage + pagePerBlock -1);
    
  }
  
  public String getAsyncPaging() {
    
    StringBuilder builder = new StringBuilder();
    
    // <
    if(beginPage == 1) {
      builder.append("<a>&lt;</a>");
    } else {
      builder.append("<a href=\"javascript:fnPaging(" + (beginPage -1) + ")\">&lt;</a>");
    }
    
    // 1 2 3 4 5 6 7 8 9 10
    for(int p = beginPage; p <= endPage; p++) {
      if(p == page) {
        builder.append("<a>" + p +"</a>");
      } else {
        builder.append("<a href=\"javascript:fnPaging(" + p + ")\">" + p + "</a>");
      }
    }
    
    // >
    if(endPage == totalPage) {
      builder.append("<a>&gt;</a>");
    } else {
      builder.append("<a href=\"javascript:fnPaging(" + (endPage + 1) + ")\">&gt;</a>");
    }
    
    return builder.toString();
  }
  
  
  // JSP DBCP 에서는 a 태그를 통해 페이징처리를 구현했지만, 지금은 SPA(싱글페이지)를 만드는거니까(=주소가 안바뀜) 기존 a 태그를 사용못함
  // spa 라는게 관건이고, ajax 처리로 getList를 자바스크립트로 가져올 거임. 자바스크립트에서 getPaging 메소드로 클릭할 때마다 작동
  // 기존의 주소를 넣는 방식의 a 태그가 아니라 <a href = "javascript:getpaging()"> 와 같이 a 태그가 주소를 바꾸는게 아닌 스크립트를 동작시키는 구조로 만들어 낼 거예용.
}
