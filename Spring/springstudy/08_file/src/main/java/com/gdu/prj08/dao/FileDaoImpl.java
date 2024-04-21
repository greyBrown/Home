package com.gdu.prj08.dao;

import org.mybatis.spring.SqlSessionTemplate;

import com.gdu.prj08.dto.FileDto;
import com.gdu.prj08.dto.HistoryDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileDaoImpl implements FileDao {
  
  private final SqlSessionTemplate sessionTemplate;


  @Override
  public int insertHistory(HistoryDto history) {
    int insertCount = sessionTemplate.insert("com.gdu.prj08.mybatis.mapper.file_t.insertHistory", history);
    return insertCount;
  }
  @Override
  public int insertFile(FileDto file) {
    int insertCount = sessionTemplate.insert("com.gdu.prj08.mybatis.mapper.file_t.insertFile", file);
    return insertCount;
  }

}
