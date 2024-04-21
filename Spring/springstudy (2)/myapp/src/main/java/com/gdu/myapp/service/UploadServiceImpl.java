package com.gdu.myapp.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.myapp.dto.AttachDto;
import com.gdu.myapp.dto.UploadDto;
import com.gdu.myapp.dto.UserDto;
import com.gdu.myapp.mapper.UploadMapper;
import com.gdu.myapp.utils.MyFileUtils;
import com.gdu.myapp.utils.MyPageUtils;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {

  private final UploadMapper uploadMapper;
  private final MyPageUtils myPageUtils;
  private final MyFileUtils myFileUtils;
  
  
  @Override
  public boolean registerUpload(MultipartHttpServletRequest multipartRequest) {
    
    // Upload_T 테이블에 추가하기(ATTACH_T 삽입을 위해 이걸 가장 먼저 처리해줌)
    String title = multipartRequest.getParameter("title");
    String contents = multipartRequest.getParameter("contents");
    int userNo = Integer.parseInt(multipartRequest.getParameter("userNo"));
    
    UserDto user = new UserDto();
    user.setUserNo(userNo);
    
    UploadDto upload = UploadDto.builder()
                          .title(title)
                          .contents(contents)
                          .user(user)
                        .build();
    System.out.println("INSERT 이전 : " + upload.getUploadNo());  // uploadNo 없음
    int insertUploadCount = uploadMapper.insertUpload(upload);
    System.out.println("INSERT 이후 : " + upload.getUploadNo());  // uploadNo 있음 (<selectKey> 동작에 의해서). 계속 이야기 나오는 SEQ 빼서 쓰는 구조!
    
    
    // 첨부 파일 처리하기
    List<MultipartFile> files = multipartRequest.getFiles("files");  
    // 폼 태그의 files 부분. multiple 이기 때문에 files 로 받는다. 만약 폼에서 multiple이 빠지고 단일첨부로 바뀐다면 file 로 받는다.
    // 여러개니까 List로 받고 이 파일 각각은 무슨 타입으로 보는가? MultipartFile 라고 부릅니당.
    
    //  System.out.println(files);
    // 첨부 파일이 없는 경우 : [MultipartFile[field="files", filename=, contentType=application/octet-stream, size=0]]    <- 파일 첨부안돼도 리스트가 존재하지만, 사이즈가 0인 것을 알 수 있음
    // 첨부 파일이 있는 경우 : [MultipartFile[field="files", filename=animal2.jpg, contentType=image/jpeg, size=311017]]
    // 리스트 배열 빼는 법 -> files[0] (x) , files.get(0) (o). 후자가 맞는 방법이었음. files.get(0) 이렇게 해서 size=0 이면 파일 없는거, size 있으면 파일있는거.
    // size 가져오는 메소드는? getSize()
    int insertAttachCount;
    if(files.get(0).getSize() == 0) {
      insertAttachCount = 1;         // 첨부가 없으면 files.size() 는 1 이다.
    } else {
      insertAttachCount = 0;         // 0으로 초기화 시켜놓고 있는 파일개수만큼 올라감.
    }
    
    for (MultipartFile multipartFile : files) {
      if(multipartFile != null && !multipartFile.isEmpty()) {     // null 아니고 공백 아니면
        
        String uploadPath = myFileUtils.getUploadPath();
        File dir = new File(uploadPath);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String originalFilename = multipartFile.getOriginalFilename();
        String filesystemName = myFileUtils.getFilesystemName(originalFilename);
        File file = new File(dir, filesystemName);
        
        try {
          multipartFile.transferTo(file); // 여기까지가 저장
          
          // 썸네일 작성
          String contentType = Files.probeContentType(file.toPath()); 
          //이미지가 아니면 썸네일로 쓸 수 없다. ContentType을 확인해주는 메소드 probeContentType
          // Path path 를 요구한다. File 타입을 Path 타입으로 바꿔서 넣어달라는 것. 바꿔주는 메소드가 있다. toPath()
          
          //  contentType이 이미지인지 확인("image/xxx(jpeg, png...)" 인지 -> 이미지가있다면(썸네일이 있다면) 썸네일을 Thumbnailator로 만들어준다.
          int hasThumbnail = contentType != null && contentType.startsWith("image") ? 1 : 0;
          if(hasThumbnail == 1) {
            // Thumbnailator를 이용한 이미지의 썸네일 만들기
            File thumbnail = new File(dir, "s_" + filesystemName); // 썸네일 이름은 smallsize란 뜻의 s_를 원래이름 앞에 붙여줌
            Thumbnails.of(file)            // 원본 이미지 파일
                      .size(96, 64)        // 가로 96px, 세로 64px. 사이즈는 1920/20 1280/20 이렇게 해서 정해줌...1/20 사이즈로 줄여넣음. 원본의 몇 % 이렇게 지정하는 방법도 있음. 사이트 참고~
                      .toFile(thumbnail);  // 썸네일 이미지 파일
          } // 썸네일이 없을 경우는 view 단에서 처리함
         
          // ATTACH_T 테이블에 추가하기(uploadNo를 몰라서 지금음 추가 못함. Upload_T에 먼저 삽입하고 ATTACH_T를 삽입해야 한다. 순서 Upload_T -> ATTACH_T)
          AttachDto attach = AttachDto.builder()
                               .uploadPath(uploadPath)
                               .filesystemName(filesystemName)
                               .originalFilename(originalFilename)
                               .hasThumbnail(hasThumbnail)
                               .uploadNo(upload.getUploadNo())     //uploadNo는 upload(UploadDto에 들어있다)
                               .build();
          
          insertAttachCount += uploadMapper.insertAttach(attach);  // 여기는 for문 내부임. += 를 해줘야 한다.
          
        } catch (Exception e) {
          e.printStackTrace();
        }
      } // if 끝
    }   // for 끝
    return (insertUploadCount == 1) && (insertAttachCount == files.size());
    // insertUploadCount는 한번 되야함. insertAttachCount는 파일첨부된 개수만큼이어야 함.
    // 첨부없을때 파일 사이즈 1 나오는데 어떻게 하는 것인지..? -> 그래서 사이즈 없을때  insertAttachCount = 1; 로 초기화해놓음
  }

  @Override
  public void loadUploadList(Model model) {
    
    /*
     *  interface UploadService {
     *     void execute(Model model);
     *  }
     *  
     *  class UploadRegisterService implements UploadService {
     *   @Override
     *   void execute(Model model) {
     *   
     *   }
     *  }
     *  
     *  class UploadService implements UploadService {
     *  @Override
     *    void execute(Model model) {
     *    
     *    }
     *   }
     *   
     *   이렇게 파라미터가 Model 하나로 통일 되어 있다면, Controller에서 request, response 등 필요한 요소들을 model에 모두 받아와서 꺼내써야 함.
     *   이런 상황이 된다면 이렇게 하는 방법이 있다는 것.
     *   세상엔 정말 다양한 코드 짜는 방법이 있다는 걸 항상 유의하기!
     */
    
    // model에 저장되어 있는 것 java 에서 빼보기(getAttribute : 스프링 5.2부터 도입. 그전에는 asMap 썼음. 모델을 Map으로 꺼내쓰는 방식)
    // 최신방식
    model.getAttribute("request");
    
    // 그 이전버전이 지원하던 방식
    Map<String, Object> modelMap = model.asMap();
    HttpServletRequest request = (HttpServletRequest) modelMap.get("request"); // MAP 은 저장을 Object로 시키니까 꺼낼때 다시 request로 캐스팅 해줘야함.
    
    int total = uploadMapper.getUploadCount();
    
    Optional<String> optDisplay = Optional.ofNullable(request.getParameter("display"));
    int display = Integer.parseInt(optDisplay.orElse("20"));
    
    Optional<String> optPage = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(optPage.orElse("1"));
    
    myPageUtils.setPaging(total, display, page);
    
    // 아직 남은 sort 를 opt 처리. sort가 전달이 안된다면 DESC
    Optional<String> optSort = Optional.ofNullable(request.getParameter("sort"));
    String sort = optSort.orElse("DESC");
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                    , "end", myPageUtils.getEnd()
                                    , "sort", sort);
    
    /*
     * total = 100, display = 20
     * 
     * page        beginNo
     *  1           100
     *  2           80
     *  3           60
     *  4           40
     *  5           20 .....  
     * 
     */
    
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("uploadList", uploadMapper.getUploadList(map));
    model.addAttribute("paging", myPageUtils.getPaging(request.getContextPath() + "/upload/list.do", sort, display));
    model.addAttribute("display", display);
    model.addAttribute("sort", sort);
    model.addAttribute("page", page);
    
  }
  
  @Override
  public void loadUploadByNo(int uploadNo, Model model) { // 화면 상단에 상세보기, 하단에 첨부목록보기. 즉 요 메소드가 첨부목록도 가지고 간다.
    model.addAttribute("upload", uploadMapper.getUploadByNo(uploadNo));  // 상세보기 
    model.addAttribute("attachList", uploadMapper.getAttachList(uploadNo)); // 첨부목록
  }
 
  @Override
  public ResponseEntity<Resource> download(HttpServletRequest request) {
    
    // 첨부 파일 정보를 DB 에서 가져오기
    int attachNo = Integer.parseInt(request.getParameter("attachNo"));
    AttachDto attach = uploadMapper.getAttachByNo(attachNo);
    
    // 첨부 파일 정보를 File 객체로 만든 뒤 Resource 객체로 변환
    File file = new File(attach.getUploadPath(), attach.getFilesystemName()); //경로, 파일명
    Resource resource = new FileSystemResource(file);
    
    // 첨부 파일 없으면 다운로드 취소
    // 이런식으로 데이터가 필요없을 때는 응답코드 만으로도 메소드 반환해주고 끝낼 수 있다.
    if(!resource.exists()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // DOWNLOAD_COUNT 증가
    uploadMapper.updateDownloadCount(attachNo);
    
    // 사용자가 다운로드 받을 파일명 결정 (originalFilename을 기본적으로 사용. originalFilename을 브라우저에 따라 다르게 인코딩 처리(주어MS))
    String originalFilename = attach.getOriginalFilename();
    String userAgent = request.getHeader("User-Agent");  // 처음에 헤더 배울때 이런게 했었죠...기억이 새록새록...
    try { // 플랫폼에 종속적인 소프트웨어들이 있는데...더이상 업데이트를 못해서 울며 겨자먹기로 그 플랫폼 써야만 하는 경우들이 있음. 수두룩함....
      
      // IE
      if(userAgent.contains("Trident")){ //Trident가 포함되어 있으면 IE
        originalFilename = URLEncoder.encode(originalFilename, "UTF-8").replace("+", ""); // +가 나오면 공백으로 바꿔준다.
      }
      // Edge
      else if(userAgent.contains("Edg")){     //Edg가 포함되어 있으면 Edge
        originalFilename = URLEncoder.encode(originalFilename, "UTF-8");
      }
      // Other 그 외 모던 브라우저들
      else {
       originalFilename = new String(originalFilename.getBytes(""), "ISO-8859-1");
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // 다운로드용 응답 헤더 설정 (HTTP 참조)
    HttpHeaders responseHeader = new HttpHeaders();
    responseHeader.add("Content-Type", "application/octet-stream");
    responseHeader.add("Content-Disposition", "attachment; filename=" + originalFilename);
    responseHeader.add("Content-Length", file.length() + ""); //file.length()가 long 이라서 "" 더해서 String 으로 만들어줌
    
    
    // 다운로드 진행
    return new ResponseEntity<Resource>(resource, responseHeader, HttpStatus.OK);
    
  }
  
  @Override
  public ResponseEntity<Resource> downloadAll(HttpServletRequest request) {
    
    // 다운로드할 모든 첨부 파일들의 정보를 DB 에서 가져오기
    int uploadNo = Integer.parseInt(request.getParameter("uploadNo"));
    List<AttachDto> attachList = uploadMapper.getAttachList(uploadNo);
    
    // 첨부 파일이 없으면 종료
    if(attachList.isEmpty()) {
      return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
    }
    
    // 임시 zip 파일 저장할 경로
    File tempDir = new File(myFileUtils.getTempPath());
    if (!tempDir.exists()) {
     tempDir.mkdirs(); 
    }
    
    // 임시 zip 파일 이름
    String tempFilename = myFileUtils.getTempFilename() + ".zip";
    
    // 임시 zip 파일 File 객체
    File tempfile = new File(tempDir, tempFilename);
    
    // 첨부 파일들을 하나씩 zip 파일로 모으기
    try {
      
      // ZipOutputStream 객체 생성
      ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(tempfile));
      
      for (AttachDto attach : attachList) {
        
        // zip 파일에 포함할 ZipEntry 객체 생성
        ZipEntry zipEntry = new ZipEntry(attach.getOriginalFilename()); // zip 파일에 들어갈 개별 파일의 이름
        
        // zip 파일에 ZipEntry 객체 명단 추가 (파일의 이름만 등록한 상황)
        zout.putNextEntry(zipEntry);
        
        // 실제 첨부 파일을 zip 파일에 등록 (첨부 파일을 읽어서 zip 파일로 보냄. 읽어들이는 건 InputStream) 
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(attach.getUploadPath(), attach.getFilesystemName())));
        zout.write(in.readAllBytes());  // 싹 다 읽어오는 메소드
        
        // 사용한 자원 정리
        in.close();
        zout.closeEntry();
        
        // DOWNLOAD_COUNT 증가
        uploadMapper.updateDownloadCount(attach.getAttachNo());
        
      } // for문 종료
      
      // zout 자원 반납
      zout.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // 다운로드 할 zip File 객체 -> Resource 객체
     Resource resource = new FileSystemResource(tempfile);
    
     // 임시파일의 이름이 숫자(TIMPESTAMP)로 되어있기 때문에 인코딩을 할 필요가 없다.
     
     // 다운로드용 응답 헤더 설정 (HTTP 참조)
     HttpHeaders responseHeader = new HttpHeaders();
     responseHeader.add("Content-Type", "application/octet-stream");
     responseHeader.add("Content-Disposition", "attachment; filename=" + tempFilename);
     responseHeader.add("Content-Length", tempfile.length() + ""); //file.length()가 long 이라서 "" 더해서 String 으로 만들어줌
     
     
     // 다운로드 진행
     return new ResponseEntity<Resource>(resource, responseHeader, HttpStatus.OK);
    
  }
  
  @Override
  public void removeTempFiles() {
    // 임시 폴더의 모든 파일 제거
    
   String tempDir = myFileUtils.getTempPath();
   File tempfile = new File(tempDir);
   File[] tempFiles = tempfile.listFiles();
   if(tempFiles != null) {
   for(File file : tempFiles) {
     file.delete();
   }
  }
  }
  
  @Override
  public UploadDto getUploadByNo(int uploadNo) {
    return uploadMapper.getUploadByNo(uploadNo);
  }
  
  @Override
  public int modifyUpload(UploadDto upload) {
    return uploadMapper.updateUpload(upload);
  }
  
  @Override
  public ResponseEntity<Map<String, Object>> getAttachList(int uploadNo) {
    return ResponseEntity.ok(Map.of("attachList", uploadMapper.getAttachList(uploadNo)));
  }
  
  @Override
  public ResponseEntity<Map<String, Object>> removeAttach(int attachNo) {
    
    // 삭제할 첨부 파일 정보를 DB 에서 가져오기
    AttachDto attach = uploadMapper.getAttachByNo(attachNo);
    
    // 파일 삭제
    File file = new File(attach.getUploadPath(), attach.getFilesystemName());
    if(file.exists()) {
      file.delete();
    }
    
    
    // 썸네일 삭제
    if(attach.getHasThumbnail() == 1) {
      File thumbnail = new File(attach.getUploadPath(), "s_" + attach.getFilesystemName());
      if(thumbnail.exists()) {
        thumbnail.delete();
      }
    }
    
    // DB 삭제
    int deleteCount = uploadMapper.deleteAttach(attachNo);
    return ResponseEntity.ok(Map.of("deleteCount", deleteCount));
  }
  
  
}
