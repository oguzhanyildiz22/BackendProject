package com.microservice.DefectService.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.DefectService.business.abstracts.DefectLocationService;
import com.microservice.DefectService.business.abstracts.VehicleDefectService;
import com.microservice.DefectService.business.abstracts.VehicleService;
import com.microservice.DefectService.business.requests.CreateDefectRequest;
import com.microservice.DefectService.business.responses.VehicleDefectResponse;
import com.microservice.DefectService.mapper.ModelMapperService;

@RestController
@RequestMapping("api/defects")
public class VehicleDefectController {
	
	@Autowired
	private VehicleDefectService vehicleDefectService;
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private DefectLocationService defectLocationService;
	
	@Autowired
	private ModelMapperService modelMapperService;

	@PostMapping("add/{vehicleId}")
	public ResponseEntity<String> addDefect(@PathVariable String vehicleId,@RequestBody CreateDefectRequest request ) throws IOException{
		
		if (vehicleService.existByVehicleId(vehicleId)) {
			
			vehicleDefectService.addDefect(request,vehicleId);
			return ResponseEntity.ok("Hata başarıyla eklendi.");
		}
		
		return new ResponseEntity<String>("bad request", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("get")
	public ResponseEntity<Page<VehicleDefectResponse>> getAll(
			@RequestParam int no,
			@RequestParam int size,
			@RequestParam String sortBy,
			@RequestParam String sortDirection){
		
		Page<VehicleDefectResponse> vehicleDefectDTOPage = vehicleDefectService.getAllDefects(no, size, sortBy, sortDirection);

	    return new ResponseEntity<>(vehicleDefectDTOPage, HttpStatus.OK);
	}
	
	@GetMapping("get/{field}")
	public ResponseEntity<Page<VehicleDefectResponse>> getAll(@PathVariable String field,
			@RequestParam int no,
			@RequestParam int size,
			@RequestParam String sortBy,
			@RequestParam String sortDirection){
		
		Page<VehicleDefectResponse> vehicleDefectDTOPage = vehicleDefectService.getAllDefectsByVehicleId(field,no, size, sortBy, sortDirection);

	    return new ResponseEntity<>(vehicleDefectDTOPage, HttpStatus.OK);
	}
		
	}
