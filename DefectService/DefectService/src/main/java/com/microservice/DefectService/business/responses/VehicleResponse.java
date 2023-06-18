package com.microservice.DefectService.business.responses;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class VehicleResponse {

	private Long id;
	
	private String vehicleId;
	
	private boolean deleted;
	
	private List<VehicleDefectResponse> defects = new ArrayList<>();
}
