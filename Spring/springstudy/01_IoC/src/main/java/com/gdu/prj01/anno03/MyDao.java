package com.gdu.prj01.anno03;

import java.sql.Connection;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyDao {
  
  private Connection con;
  private MyConnection myConnection;
  
//  private Connection getConnection() {
//    Connection con = null;                // myConnection 에서 try 처리가 되어있는 것을 받아오기만 하므로 여기서는 해줄 필요 없음
//    AbstractApplicationContext ctx = new GenericXmlApplicationContext("com/gdu/prj01/xml03/app-context.xml");
//    myConnection = ctx.getBean("myConnection", MyConnection.class);   //객체명, 객체타입. 생소하니까 좀 헷갈림...
//    con = myConnection.getConnetcion();
//    ctx.close();
//    return con;
//  }
  
  private void close() {
    try {                               //여기서는 con 때문에 try 처리가 필요해진다.
      if(con != null) {
        con.close();                
        System.out.println(myConnection.getUser() + " 접속해제되었습니다.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void add() {
    con = myConnection.getConnetcion();
    System.out.println("MyDao add() 호출");
    close();
  }
    
}
