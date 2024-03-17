package com.gdu.prj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class StudentDto {

	String name;
	int korean;
	int english;
	int math;
	
	
}
