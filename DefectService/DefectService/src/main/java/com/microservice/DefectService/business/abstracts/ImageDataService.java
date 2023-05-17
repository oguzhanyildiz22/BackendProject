package com.microservice.DefectService.business.abstracts;

public interface ImageDataService {

	void saveImage(String vehicleId, byte[] image) throws Exception;
	
	byte[] getImage(String vehicleId) throws Exception;
}
