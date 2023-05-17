package com.microservice.DefectService.business.abstracts;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestHeader;

import com.microservice.DefectService.business.requests.CreateVehicleRequest;
import com.microservice.DefectService.business.responses.VehicleResponse;

public interface VehicleService {

	void add(CreateVehicleRequest createVehicleRequest);
	
	boolean existByVehicleId(String vehicleId);

	Page<VehicleResponse> getVehicles(int no,int size , String sortBy,String sortDirection);

	void delete(String vehicleId);
	
	boolean checkOperatorRole(@RequestHeader("Authorization") String authorizationHeader);
	
	boolean checkTeamLeaderRole(@RequestHeader("Authorization") String authorizationHeader);
	
	
	
}
