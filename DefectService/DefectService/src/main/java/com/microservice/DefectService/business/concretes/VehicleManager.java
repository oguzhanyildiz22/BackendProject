package com.microservice.DefectService.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

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
public class VehicleManager implements VehicleService{

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private ModelMapperService modelMapperService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RestTemplateConfig config;
	
	
	@Override
	public void add(CreateVehicleRequest createVehicleRequest) {
		
		if (existByVehicleId(createVehicleRequest.getVehicleId())) {
			throw new RuntimeException("there is already in use");
		}
			Vehicle vehicle = new Vehicle();
			vehicle.setVehicleId(createVehicleRequest.getVehicleId());
	
	     vehicleRepository.save(vehicle);
	     
		}

	@Override
	public boolean existByVehicleId(String vehicleId) {
		if (vehicleRepository.existsByVehicleId(vehicleId)) {
			return true;
		}
		return false;
	}

	@Override
	public Page<VehicleResponse> getVehicles(int no, int size , String sortBy, String sortDirection) {
		Sort.Direction direction = Sort.Direction.fromString(sortDirection);
		Pageable pageable = PageRequest.of(no, size, direction, sortBy);
		Page<Vehicle> pageVehicles = vehicleRepository.findAll(pageable);
		List<VehicleResponse> responseList = pageVehicles.stream()
	    	    .map(defect -> modelMapperService.forResponse().map(defect, VehicleResponse.class))
	    	    .collect(Collectors.toList());
		return  new PageImpl<>(responseList, pageable, pageVehicles.getTotalElements());
	}

	@Override
	public void delete(String vehicleId) {
		Vehicle vehicle = vehicleRepository.findByVehicleId(vehicleId);
		vehicleRepository.delete(vehicle);
    }

	@Override
	public boolean checkOperatorRole(String authorizationHeader) {
		
		
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", authorizationHeader);
	        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

	        ResponseEntity<String> response = restTemplate.exchange(config.getUrl1(), HttpMethod.GET, entity, String.class);

	        String roles = response.getBody();

	        if (roles.contains("OPERATOR")) {
	           
	            return true;
	        }

	        
	        return false;
	    
	}

	@Override
	public boolean checkTeamLeaderRole(String authorizationHeader) {
		
		    HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", authorizationHeader);
	        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

	        ResponseEntity<String> response = restTemplate.exchange(config.getUrl1(), HttpMethod.GET, entity, String.class);

	        String roles = response.getBody();

	        if (roles.contains("TEAM_LEADER")) {
	           
	            return true;
	        }

	        
	        return false;
	}
	
	
	
		
}
