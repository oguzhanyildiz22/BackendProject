package com.microservice.DefectService.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.DefectService.business.abstracts.VehicleService;
import com.microservice.DefectService.business.requests.CreateVehicleRequest;
import com.microservice.DefectService.business.responses.VehicleResponse;

@RestController
@RequestMapping("api/vehicle")
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;
	
	private static final Logger logger = LogManager.getLogger(VehicleController.class);
	
	@PostMapping("/add")
	public ResponseEntity<String> addVehicle(@RequestBody CreateVehicleRequest request,
	        @RequestHeader("Authorization") String authorizationHeader) {
	    
	    logger.info("AddVehicle endpoint called with authorization header: {}", authorizationHeader);
	    
	    if (vehicleService.checkOperatorRole(authorizationHeader)) {
	        logger.info("User has OPERATOR role. Adding vehicle with request: {}", request);
	        
	        vehicleService.add(request);
	        
	        return ResponseEntity.ok("Araç başarıyla eklendi.");
	    } else {
	        logger.warn("User does not have OPERATOR role. Access unauthorized.");
	        return new ResponseEntity<>("Araç eklenemedi", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	@GetMapping("/get")
	public ResponseEntity<Page<VehicleResponse>> getVehicles(@RequestParam int no,
	        @RequestParam int size,
	        @RequestParam String sortBy,
	        @RequestParam String sortDirection,
	        @RequestHeader("Authorization") String authorizationHeader) {
	    
	    logger.info("GetVehicles endpoint called with authorization header: {}", authorizationHeader);
	    
	    if (vehicleService.checkTeamLeaderRole(authorizationHeader)) {
	        logger.info("User has TEAM_LEADER role. Retrieving vehicles with parameters: no={}, size={}, sortBy={}, sortDirection={}", no, size, sortBy, sortDirection);
	        
	        Page<VehicleResponse> vehicles = vehicleService.getVehicles(no, size, sortBy, sortDirection);
	        
	        return new ResponseEntity<>(vehicles, HttpStatus.OK);
	    } else {
	        logger.warn("User does not have TEAM_LEADER role. Access unauthorized.");
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }
	}
	
	
	@DeleteMapping("/{vehicleId}")
	public String delete(@PathVariable String vehicleId,
	        @RequestHeader("Authorization") String authorizationHeader) {
	    
	    logger.info("Delete endpoint called with vehicleId: {} and authorization header: {}", vehicleId, authorizationHeader);
	    
	    if (vehicleService.checkOperatorRole(authorizationHeader)) {
	        logger.info("User has OPERATOR role. Deleting vehicle with vehicleId: {}", vehicleId);
	        
	        vehicleService.delete(vehicleId);
	        return "deleted";
	    } else {
	        logger.warn("User does not have OPERATOR role. Vehicle not deleted.");
	        return "vehicle not deleted";
	    }
	}
}
