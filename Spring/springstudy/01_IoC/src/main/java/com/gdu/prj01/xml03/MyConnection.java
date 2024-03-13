package com.gdu.prj01.xml03;

import java.sql.Connection;
import java.sql.DriverManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyConnection {

  private String driver;
  private String url;
  private String user;
  private String password;
  
  public Connection getConnetcion() {
    Connection con = null;       // 반환타입 있는거면 그거랑 return 먼저 만들어주기~
    try {
      Class.forName(driver);
      con = DriverManager.getConnection(url, user, password);
      System.out.println(user + " 접속되었습니다.");
      
    } catch (Exception e) {
        e.printStackTrace();
    }
    return con;
  }
  
  
  
  
}
