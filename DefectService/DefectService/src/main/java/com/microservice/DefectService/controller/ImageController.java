package com.microservice.DefectService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.microservice.DefectService.business.abstracts.ImageDataService;
import com.microservice.DefectService.business.abstracts.VehicleService;


@RestController
@RequestMapping("api/image")
public class ImageController {
	
	@Autowired
	private ImageDataService service;
	
	@Autowired
	private VehicleService vehicleService;

	@PostMapping("/save/{vehicleId}")
	public String saveImage(@PathVariable String vehicleId, MultipartFile file) throws Exception {
		
		if (vehicleService.existByVehicleId(vehicleId)) {
			byte[] image = file.getBytes();
			service.saveImage(vehicleId, image);
			return "resim başrıyla kaydedildi";
		}
		
		return "araç bulunamadı : " +vehicleId ;
	}
	
	@GetMapping("/get-image/{vehicleId}")
	public ResponseEntity<byte[]> getImage(@PathVariable String vehicleId) throws Exception {
		
		HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.IMAGE_PNG);
		
		 byte[] savedImage = service.getImage(vehicleId);
		 return new ResponseEntity<>(savedImage, headers, HttpStatus.OK);
	}
	
	

}
