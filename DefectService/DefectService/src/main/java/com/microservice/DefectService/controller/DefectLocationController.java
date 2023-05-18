package com.microservice.DefectService.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	private static final Logger logger = LogManager.getLogger(DefectLocationController.class);


	@GetMapping("/get")
	public ResponseEntity<Page<DefectLocationResponse>> getDefects(@RequestParam int no,
			@RequestParam int size,
			@RequestParam String sortBy,
			@RequestParam String sortDirection,
			@RequestHeader("Authorization") String authorizationHeader) {
		logger.info("Get Locations of Defect endpoint called with no: {}, size: {}, sortBy: {}, sortDirection: {}, and authorization header: {}",
				no, size, sortBy, sortDirection, authorizationHeader);
		
		if (vehicleService.checkTeamLeaderRole(authorizationHeader)) {
			logger.info("User has TEAM LEADER role.");
			
			Page<DefectLocationResponse> vehicles = defectLocationService.getVehicles(no, size, sortBy, sortDirection);
			return new ResponseEntity<>(vehicles, HttpStatus.OK);
		}
		
		logger.warn("Unauthorized access to Get Vehşcles endpoint.");
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
