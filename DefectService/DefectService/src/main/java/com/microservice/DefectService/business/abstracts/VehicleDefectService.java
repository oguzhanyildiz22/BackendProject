package com.microservice.DefectService.business.abstracts;

import java.io.IOException;

import org.springframework.data.domain.Page;

import com.microservice.DefectService.business.requests.CreateDefectRequest;
import com.microservice.DefectService.business.responses.VehicleDefectResponse;

public interface VehicleDefectService {

	void addDefect(CreateDefectRequest request,String vehicleId) throws IOException;
	
	// byte[] getImage(String vehicleId) throws Exception;
	

	Page<VehicleDefectResponse> getAllDefects(int no, int size, String sortBy, String sortDirection);

	Page<VehicleDefectResponse> getAllDefectsByVehicleId(String vehicleId, int no, int size, String sortBy,
			String sortDirection);
	
	
}
