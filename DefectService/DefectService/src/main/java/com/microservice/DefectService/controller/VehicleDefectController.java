package com.microservice.DefectService.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger logger = LogManager.getLogger(VehicleDefectController.class);

	@PostMapping("add/{vehicleId}")
	public ResponseEntity<String> addDefect(@PathVariable String vehicleId, @ModelAttribute CreateDefectRequest request,
			@RequestHeader("Authorization") String authorizationHeader) throws IOException {

		logger.info("Add Defect endpoint called with vehicleId: {} and authorization header: {}", vehicleId,
				authorizationHeader);

		if (vehicleService.checkOperatorRole(authorizationHeader)) {
			logger.info("User has OPERATOR role.");

			if (vehicleService.existByVehicleId(vehicleId)) {
				logger.info("Vehicle exists with vehicleId: {}. Adding defect.", vehicleId);

				vehicleDefectService.addDefect(request, vehicleId);
				return ResponseEntity.ok("The defect has been successfully added");
			}
		}

		logger.warn("User does not have OPERATOR authority. Defect not added.");
		return new ResponseEntity<String>("Unauthorized action", HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("get")
	public ResponseEntity<Page<VehicleDefectResponse>> getAll(@RequestParam int no, @RequestParam int size,
			@RequestParam String sortBy, @RequestParam String sortDirection,
			@RequestHeader("Authorization") String authorizationHeader) {

		logger.info(
				"Get All Defects endpoint called with no: {}, size: {}, sortBy: {}, sortDirection: {}, and authorization header: {}",
				no, size, sortBy, sortDirection, authorizationHeader);

		if (vehicleService.checkTeamLeaderRole(authorizationHeader)) {
			logger.info("User has TEAM LEADER role.");

			Page<VehicleDefectResponse> vehicleDefectDTOPage = vehicleDefectService.getAllDefects(no, size, sortBy,
					sortDirection);
			return new ResponseEntity<>(vehicleDefectDTOPage, HttpStatus.OK);
		}

		logger.warn("User does not have TEAM_LEADER authority. getAll method was not started.");
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}


	@GetMapping("get-image/{vehicleId}")
	public ResponseEntity<byte[]> getImage(@PathVariable String vehicleId,
			@RequestHeader("Authorization") String authorizationHeader) throws IOException {

		logger.info("Get Image endpoint called for vehicleId: {} with authorization header: {}", vehicleId,
				authorizationHeader);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);

		if (vehicleService.checkTeamLeaderRole(authorizationHeader)) {
			logger.info("User has TEAM LEADER role. Retrieving image for vehicleId: {}", vehicleId);

			byte[] defectImage = vehicleDefectService.getImage(vehicleId);
			return new ResponseEntity<>(defectImage, headers, HttpStatus.OK);

		}
		logger.warn("Unauthorized access to Get Image endpoint for vehicleId: {}", vehicleId);

		byte[] nullImage = null;
		return new ResponseEntity<>(nullImage, headers, HttpStatus.UNAUTHORIZED);
	}

	

}
