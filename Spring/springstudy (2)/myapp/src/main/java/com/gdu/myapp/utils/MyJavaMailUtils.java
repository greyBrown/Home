package com.gdu.myapp.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource(value = "classpath:email.properties")            // properties 들어가는 resources!! 그 resources 까지가 classpath 이다.
@Component
public class MyJavaMailUtils {

  @Autowired     // 기본 제공되는 bean임. 이미 존재하므로 주입해주기만 하면 된다.
  private Environment env;  
  
  public void sendMail(String to, String subject, String content) {
    
    // 이메일을 보내는 호스트의 정보 : 구글  (아래의 입력은 구글의 메뉴얼대로 약속된 입력양식임)
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");                 //메소드 모양 보면 알겠지만 Map 이랑 비슷하게 생긴 데이터형식이다.
    props.put("mail.smtp.port", 587);
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.starttls.enable", true);
    
    // javax.mail.Session 객체 생성 : 이메일을 보내는 사용자의 정보 (개인 정보)
    Session session = Session.getInstance(props, new Authenticator() {      // 이러면 자동으로!!! 안나오지만..컨스 하면 나옵니다 헤헤
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password")); // 이 부분이 깃에 올라가면 안되는 부분. 별도의 파일로 빼고 ignore 처리를 한다.
      }
    });
    
    try {
      
      // 메일 만들기 (보내는 사람 + 받는 사람 + 제목 + 내용)
      MimeMessage mimeMessage = new MimeMessage(session); // 바로 위에서 만든 session(보내는 호스트정보가 들어있음)
      mimeMessage.setFrom(new InternetAddress(env.getProperty("spring.mail.username"), "myapp")); //gmail 부분은 마찬가지로 ignore 처리. 뒷부분은 별명같은거(메일함에 뜨는 그 이름 ㅋㅋ). 보내는 사람은 정해져있다.
      mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 받는 사람의 이메일 to. 파라미터로 받아온다.
      mimeMessage.setSubject(subject);                                             // subject. 제목도 파라미터로 받아온다
      mimeMessage.setContent(content, "text/html; charset=UTF-8");                                   // 내용도 파라미터로 받아온다.
      // 바뀔 수 있는 것은 서비스에서 파라미터로 받아온다. 
      // (즉 모듈화를 통해 비즈니스로직이 바뀐다면(임시비번발급, 인증) 바뀌는 서비스에 따라 받아오는 파라미터를 달리할 수 있다._
      // 비밀번호 찾기 - 임시입력번호 발급을 프로젝트 때 구현해보는 것도 좋다.
      
      
      // 메일 보내기
      Transport.send(mimeMessage);
      
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
    
    
  }
  
  
  
  
 
}
