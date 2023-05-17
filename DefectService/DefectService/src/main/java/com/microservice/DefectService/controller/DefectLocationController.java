package com.microservice.DefectService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.DefectService.business.abstracts.DefectLocationService;
import com.microservice.DefectService.business.abstracts.VehicleService;
import com.microservice.DefectService.business.responses.DefectLocationResponse;

@RestController
@RequestMapping("api/location")
public class DefectLocationController {
	
	@Autowired
	private DefectLocationService defectLocationService;
	
	@Autowired
	private VehicleService vehicleService;

	@GetMapping("/get")
	public ResponseEntity<Page<DefectLocationResponse>> getSehirler(@RequestParam int no,
			@RequestParam int size,
			@RequestParam String sortBy,
			@RequestParam String sortDirection,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (vehicleService.checkTeamLeaderRole(authorizationHeader)) {
			Page<DefectLocationResponse> vehicles = defectLocationService.getVehicles(no, size,sortBy,sortDirection);
			 return new ResponseEntity<>(vehicles, HttpStatus.OK);
		}
		return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
	}

}
