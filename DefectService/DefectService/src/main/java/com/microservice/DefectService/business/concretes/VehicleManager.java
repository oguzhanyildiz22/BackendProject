package com.microservice.DefectService.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.DefectService.business.abstracts.VehicleService;
import com.microservice.DefectService.business.requests.CreateVehicleRequest;
import com.microservice.DefectService.business.responses.VehicleResponse;
import com.microservice.DefectService.config.RestTemplateConfig;
import com.microservice.DefectService.entity.Vehicle;
import com.microservice.DefectService.mapper.ModelMapperService;
import com.microservice.DefectService.repository.VehicleRepository;

@Service
public class VehicleManager implements VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private ModelMapperService modelMapperService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RestTemplateConfig config;

	private static final Logger logger = LogManager.getLogger(VehicleManager.class);

	@Override
	public void add(CreateVehicleRequest createVehicleRequest) {
	
		String vehicleId = createVehicleRequest.getVehicleId();
		
		logger.info("Begin add method to add vehicle with vehicleId: {}", vehicleId);

		if (vehicleRepository.existsByVehicleId(vehicleId)) {
			logger.error("Vehicle with vehicleId {} already exists.", vehicleId);
		}

		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleId(vehicleId);

		vehicleRepository.save(vehicle);

		logger.info("Vehicle added with vehicleId: {}", vehicleId);
	}

	@Override
	public boolean existByVehicleId(String vehicleId) {
		
	    logger.info("Begin existByVehicleId method to Check existence of vehicle with vehicleId: {}", vehicleId);

		boolean exists = vehicleRepository.existsByVehicleId(vehicleId);

		if (exists) {
			logger.debug("Vehicle with vehicleId {} exists.", vehicleId);
		} else {
			logger.debug("Vehicle with vehicleId {} does not exist.", vehicleId);
		}

		return exists;
	}

	
	@Override
	public Page<VehicleResponse> getVehicles(int no, int size, String sortBy, String sortDirection) {
		
		logger.info("Begin getVehicles method to Fetch vehicles with pagination - Page: {}, Size: {}, SortBy: {}, SortDirection: {}",
	            no, size, sortBy, sortDirection);
		Sort.Direction direction = Sort.Direction.fromString(sortDirection);
		Pageable pageable = PageRequest.of(no, size, direction, sortBy);
		Page<Vehicle> pageVehicles = vehicleRepository.findAllActive(pageable);
		List<VehicleResponse> responseList = pageVehicles.stream()
				.map(vehicle -> modelMapperService.forResponse().map(vehicle, VehicleResponse.class))
				.collect(Collectors.toList());

		logger.debug("Retrieved {} vehicles. Pagination: page={}, size={}, sortBy={}, sortDirection={}",
				pageVehicles.getTotalElements(), no, size, sortBy, sortDirection);

		return new PageImpl<>(responseList, pageable, pageVehicles.getTotalElements());
	}

	@Override
	public void delete(String vehicleId) {
		
		logger.info("Begin delete method to soft delete vehicle with vehicleId: {}", vehicleId);
		Vehicle vehicle = vehicleRepository.findByVehicleId(vehicleId);
		if (vehicle != null) {
			vehicleRepository.delete(vehicle);
			logger.info("Vehicle with ID {} deleted successfully.", vehicleId);
		} else {
			logger.warn("Vehicle with ID {} not found. Deletion failed.", vehicleId);
		}
	}

	@Override
	public boolean checkOperatorRole(String authorizationHeader) {
		
	    logger.info("Begin checkOperatorRole method to check operator role for authorization header: {}", authorizationHeader);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authorizationHeader);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(config.getUrl1(), HttpMethod.GET, entity,
					String.class);
			String roles = response.getBody();

			if (roles.contains("OPERATOR")) {
				logger.info("Operator role check passed for authorization header: {}", authorizationHeader);
				return true;
			} else {
				logger.warn("Operator role not found for authorization header: {}", authorizationHeader);
				return false;
			}
		} catch (Exception e) {
			logger.error("Error occurred while checking operator role for authorization header: {}",
					authorizationHeader, e);
			return false;
		}
	}

	@Override
	public boolean checkTeamLeaderRole(String authorizationHeader) {
		
	    logger.info("Begin checkTeamLeaderRole method to check operator role for authorization header: {}", authorizationHeader);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authorizationHeader);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(config.getUrl1(), HttpMethod.GET, entity,
					String.class);
			String roles = response.getBody();

			if (roles.contains("TEAM_LEADER")) {
				logger.info("Team Leader role check passed for authorization header: {}", authorizationHeader);
				return true;
			} else {
				logger.warn("Team Leader role not found for authorization header: {}", authorizationHeader);
				return false;
			}
		} catch (Exception e) {
			logger.error("Error occurred while checking team leader role for authorization header: {}",
					authorizationHeader, e);
			return false;
		}
	}

}
