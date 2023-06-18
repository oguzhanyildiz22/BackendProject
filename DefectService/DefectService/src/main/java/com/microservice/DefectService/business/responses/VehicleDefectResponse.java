package com.microservice.DefectService.business.responses;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
@Data
public class VehicleDefectResponse {

	 
	    private Long id;  
	    
	    private String defectType;

	    private List<DefectLocationResponse> locations = new ArrayList<>();
}
