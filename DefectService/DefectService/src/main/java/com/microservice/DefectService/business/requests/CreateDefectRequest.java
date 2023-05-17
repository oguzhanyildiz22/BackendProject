package com.microservice.DefectService.business.requests;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDefectRequest {

	
	private String defectType;
	private List<CreateDefectLocations> defectLocations = new ArrayList<>();
	
}
