package com.microservice.DefectService.business.abstracts;

import org.springframework.data.domain.Page;

import com.microservice.DefectService.business.responses.DefectLocationResponse;

public interface DefectLocationService {

	Page<DefectLocationResponse> getVehicles(int no, int size, String sortBy, String sortDirection);

//	byte[] getMarkedImage(String vehicleId) throws IOException, Exception;
}
