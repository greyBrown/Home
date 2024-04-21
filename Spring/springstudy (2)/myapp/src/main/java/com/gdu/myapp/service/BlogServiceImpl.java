package com.gdu.myapp.service;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.events.Comment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.myapp.dto.BlogDto;
import com.gdu.myapp.dto.CommentDto;
import com.gdu.myapp.dto.UserDto;
import com.gdu.myapp.mapper.BlogMapper;
import com.gdu.myapp.utils.MyFileUtils;
import com.gdu.myapp.utils.MyPageUtils;
import com.gdu.myapp.utils.MySecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

  private final BlogMapper blogMapper;
  private final MyPageUtils myPageUtils;
  private final MyFileUtils myFileUtils;
  
  @Override
  public ResponseEntity<Map<String, Object>> summernoteImageUpload(MultipartFile multipartFile) {
    
    // 이미지 저장할 경로 생성
    String uploadPath = myFileUtils.getUploadPath();
    File dir = new File(uploadPath);
    if(!dir.exists()) {
      dir.mkdirs();
    }
    
    // 저장할 이름 생성
    String filesystemName = myFileUtils.getFilesystemName(multipartFile.getOriginalFilename());
    
    
    // 이미지 저장
    File file = new File(dir, filesystemName);
    try {
      multipartFile.transferTo(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
     
    
   
    Map<String, Object> map = Map.of("src", uploadPath + "/" + filesystemName);
    
    // contextPath 문제 때문에 그냥 다시 request 하나 넣어줬지만...잘 돌아가는데 괜히 오류 날 것 같아서 servlet에서 뺀 거 쓰겠음.
    // 나중에 문제생기면 강사님 깃 보고 contextPath request로 뽑아서 넣기...ㅎㅎ.....
    // 원래 웹 관련한거(서블릿같은거)는 컨트롤러에서 처리하고 매개변수로 받아오는게 맞는 방법임! 그게 더 안정성이 높고 뭐시기뭐시기....의존성과 유지보수성....
    // 하지만 지금은 그거 고치다가 잘 돌아가는거 변경하기 싫죠?????????? + 강사님이 마지막에 찾아내신 것. "그냥 jsp 에서도 붙였어도 됐네!!" -> 일케 변경함 ㅋㅋ
    
    // 미리보기를 위해 필요한게 있죠?? <resources mapping="/upload/**" location="file:///upload/"/> 이거!! 이거이거 다시 찾아보기
    // 가물가물하면 가서 한번 설명 더 보기 ...ㅜ
    
    // 반환
    return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
  }

  @Override
  public int registerBlog(HttpServletRequest request) {
    
    
    // 요청 파라미터
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    // UserDto + BlogDto 객체 생성
    UserDto user = new UserDto();
    user.setUserNo(userNo);  // 오랜만에 set 방식으로 한번~
    BlogDto blog = BlogDto.builder()
                     .title(MySecurityUtils.getPreventXss(title))
                     .contents(MySecurityUtils.getPreventXss(contents))
                     .user(user)
                   .build();
                      
    
    /* summernote 편집기에서 사용한 이미지 확인하는 방법 */
    /* 태그를 분석하는 자바 라이브러리 사용 권장(Jsoup 라이브러리 maven - Jsoup Java HTML Parser - 1.15.4). Parser를 통해 여기서도 HTML을 가져올수 있다.*/
    Document document =  Jsoup.parse(contents);  //(JSOUP 의 document를 선언해준다. 자바의 Document 와 비슷하지만 JSOUP 전용 그런 느낌)
    Elements elements = document.getElementsByTag("img"); //elements(복수) element(단수) -> Document(DOM)의 하위요소
    if(elements != null) {  // dom 으로 추출해냈으니 이제 반복문에 넣어서 돌릴 수 있다.  
      for(Element element : elements) {  // 이미지태그들에서 이미지태그 하나씩 뽑아 for문을 돌린다.
        String src = element.attr("src");                  // 이미지태그의 attributes인 src를 뽑아낸다. 이렇게 뽑아낸 src를 다양하게 활용(DB에 넣거나 이름만 짤라내거나 등등)
        /* src 정보를 DB에 저장하는 코드 등이 이 곳에 있으면 된다. 이미지 경로나....삭제할 때 그래야 그 경로 찾아가서 실제 이미지 삭제함*/
        System.out.println(src);
        // /myapp/upload/2024/04/08/6378b5d4275d4138bf657431d2e67369.jpg이게 뜬다. src가 뜸. 이걸 자르고 붙여서 경로랑 이름만 뽑거나 등등
      }
    }
    // DB에 blog 저장
    return blogMapper.insertBlog(blog);
  }

  @Override
  public ResponseEntity<Map<String, Object>> getBlogList(HttpServletRequest request) {
    
    // 전체 블로그 개수
    int total = blogMapper.getBlogCount();
    
    // 스크롤 이벤트마다 가져갈 목록 개수
    int display = 10;
    
    // 현재 페이지 번호
    int page = Integer.parseInt(request.getParameter("page"));
    // opt 처리를 안 한 이유는 ajax 처리를 할 것이기 때문에 안넘어올 상황이 거의 없기 때문...하지만 넣어줘도 전혀 상관없음! 지금은 그냥 빼봤어요
    
    // 페이징 처리
    myPageUtils.setPaging(total, display, page);
    
    // 목록 가져올 때 전달할 Map 생성
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                    , "end", myPageUtils.getEnd());
    
    // 목록 화면으로 반환할 값 (목록 + 전체 페이지 수) 
    
  
    return new ResponseEntity<>(Map .of("blogList", blogMapper.getBlogList(map)
                                      , "totalPage", myPageUtils.getTotalPage())
                                        , HttpStatus.OK);
    
  }
  
  @Override
  public int updateHit(int blogNo) {
    return blogMapper.updateHit(blogNo);
  }
  
  
  
  @Override
  public BlogDto getBlogByNo(int blogNo) {
    return blogMapper.getBlogByNo(blogNo);
  }
  
  @Override
  public int registerComment(HttpServletRequest request) {

    // 요청 파라미터
   String contents = MySecurityUtils.getPreventXss(request.getParameter("contents"));
   int blogNo = Integer.parseInt(request.getParameter("blogNo"));
   int userNo = Integer.parseInt(request.getParameter("userNo"));
   
   // UserDto 객체 생성
   UserDto user = new UserDto();
   user.setUserNo(userNo);
   
   // CommentDto 객체 생성
   CommentDto comment = CommentDto.builder()
                          .contents(contents)
                          .user(user)
                          .blogNo(blogNo)
                          .build();
   
   // DB 에 저장 & 결과 반환
    return blogMapper.insertComment(comment);
  }
  
  @Override
  public Map<String, Object> getCommentList(HttpServletRequest request) {
   
   // 요청 파라미터
    int blogNo = Integer.parseInt(request.getParameter("blogNo"));
    int page =  Integer.parseInt(request.getParameter("page"));
    
    // 전체 댓글 개수
    int total = blogMapper.getCommentCount(blogNo);
    
    // 한 페이지에 표시할 댓글 개수
    int display = 10;
    
    // 페이징 처리
    myPageUtils.setPaging(total, display, page);
    
    // 목록을 가져올 때 사용할 Map 생성
    Map<String, Object> map = Map.of("blogNo", blogNo, "begin", myPageUtils.getBegin(), "end", myPageUtils.getEnd());
    
    // 결과 (목록, 페이징) 반환. ajax 처리를 위해 myPageUtils 에 만들어두었던 getAsyncPaging. 주소가 아닌 자바스크립트 함수 호출이 달려있다!
    return Map.of("commentList", blogMapper.getCommentList(map)
                 , "paging", myPageUtils.getAsyncPaging());
  }
  
  @Override
  public int registerReply(HttpServletRequest request) {
    
    // 요청 파라미터
    String contents = request.getParameter("contents");
    int groupNo = Integer.parseInt(request.getParameter("groupNo"));
    int blogNo = Integer.parseInt(request.getParameter("blogNo"));
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    // UserDto 객체 생성
    UserDto user = new UserDto();
    user.setUserNo(userNo);
    
    // CommentDto 객체 생성
    CommentDto reply = CommentDto.builder()
                         .contents(contents)
                         .groupNo(groupNo)
                         .blogNo(blogNo)
                         .user(user)
                         .build();
    
    // DB 에 저장하고 결과 반환
    return blogMapper.insertReply(reply);
  }
  
  @Override
  public int removeComment(int commentNo) {

    return blogMapper.removeComment(commentNo);
  }
  
  @Override
  public int modifyBlog(int blogNo) {
    // TODO Auto-generated method stub
    return 0;
  }
  
  
}
