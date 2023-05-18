package com.microservice.DefectService.business.requests;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDefectRequest {

	
	private String defectType;
	private MultipartFile file;
	private List<CreateDefectLocations> defectLocations;
	
}
