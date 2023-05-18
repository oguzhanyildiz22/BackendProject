package com.microservice.DefectService.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.DefectService.business.abstracts.VehicleDefectService;
import com.microservice.DefectService.business.abstracts.VehicleService;
import com.microservice.DefectService.business.requests.CreateDefectRequest;
import com.microservice.DefectService.business.responses.VehicleDefectResponse;

@RestController
@RequestMapping("api/defects")
public class VehicleDefectController {
	
	@Autowired
	private VehicleDefectService vehicleDefectService;
	@Autowired
	private VehicleService vehicleService;
	
	

	@PostMapping("add/{vehicleId}")
	public ResponseEntity<String> addDefect(@PathVariable String vehicleId,
			@ModelAttribute CreateDefectRequest request,
			@RequestHeader("Authorization") String authorizationHeader ) throws IOException{
		if (vehicleService.checkOperatorRole(authorizationHeader)) {
			
			if (vehicleService.existByVehicleId(vehicleId)) {
				
				vehicleDefectService.addDefect(request,vehicleId);
				return ResponseEntity.ok("Hata başarıyla eklendi.");
			}
			
		}
		
		return new ResponseEntity<String>("bad request", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("get")
	public ResponseEntity<Page<VehicleDefectResponse>> getAll(
			@RequestParam int no,
			@RequestParam int size,
			@RequestParam String sortBy,
			@RequestParam String sortDirection,
			@RequestHeader("Authorization") String authorizationHeader ){
		if (vehicleService.checkTeamLeaderRole(authorizationHeader)) {
			Page<VehicleDefectResponse> vehicleDefectDTOPage = vehicleDefectService.getAllDefects(no, size, sortBy, sortDirection);

		    return new ResponseEntity<>(vehicleDefectDTOPage, HttpStatus.OK);
		}
	    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

	}
	
	@GetMapping("get/{field}")
	public ResponseEntity<Page<VehicleDefectResponse>> getAll(@PathVariable String field,
			@RequestParam int no,
			@RequestParam int size,
			@RequestParam String sortBy,
			@RequestParam String sortDirection,
			@RequestHeader("Authorization") String authorizationHeader){
		if (vehicleService.checkTeamLeaderRole(authorizationHeader)) {
			Page<VehicleDefectResponse> vehicleDefectDTOPage = vehicleDefectService.getAllDefectsByVehicleId(field,no, size, sortBy, sortDirection);

		    return new ResponseEntity<>(vehicleDefectDTOPage, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("get-image/{vehicleId}")
	public ResponseEntity<byte[]> getImage(@PathVariable String vehicleId,
			@RequestHeader("Authorization") String authorizationHeader) throws IOException{
		
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.IMAGE_PNG);	
		
		 if (vehicleService.checkTeamLeaderRole(authorizationHeader)) {
			
				 
			
			 byte[] defectImage = vehicleDefectService.getImage(vehicleId);
			 return new ResponseEntity<>(defectImage, headers, HttpStatus.OK);
			 
		}
		 byte[] nullImage = null ;
		 return new ResponseEntity<>(nullImage, headers, HttpStatus.UNAUTHORIZED);
	}
 		
	}
