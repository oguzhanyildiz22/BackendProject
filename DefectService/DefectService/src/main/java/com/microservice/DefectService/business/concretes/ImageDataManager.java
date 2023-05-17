package com.microservice.DefectService.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.DefectService.business.abstracts.ImageDataService;
import com.microservice.DefectService.entity.ImageData;
import com.microservice.DefectService.entity.Vehicle;
import com.microservice.DefectService.repository.ImageDataRepository;
import com.microservice.DefectService.repository.VehicleRepository;

@Service
public class ImageDataManager  implements ImageDataService{
	
	@Autowired 
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private ImageDataRepository imageDataRepository;

	@Override
	public void saveImage(String vehicleId, byte[] image) throws Exception {
		
		Vehicle vehicle = vehicleRepository.findByVehicleId(vehicleId);
		
		
		ImageData imageData = new ImageData();
		imageData.setImage(image);
		imageData.setVehicle(vehicle);
		imageDataRepository.save(imageData);
		
		
	}
	
	@Override
	public byte[] getImage(String vehicleId) throws Exception {
	    ImageData imageData = imageDataRepository.findByVehicleId(vehicleId);
		if (imageData == null) {
			throw new Exception("there is no imageData with this" + vehicleId);
		}
		byte[] image = imageData.getImage();
		if (image == null) {
			throw new Exception("image is null");
		}
		
		return image;
	}

}
