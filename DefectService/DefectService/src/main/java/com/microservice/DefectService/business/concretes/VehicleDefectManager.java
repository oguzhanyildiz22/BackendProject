package com.microservice.DefectService.business.concretes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.microservice.DefectService.business.abstracts.VehicleDefectService;
import com.microservice.DefectService.business.requests.CreateDefectRequest;
import com.microservice.DefectService.business.responses.VehicleDefectResponse;
import com.microservice.DefectService.entity.DefectLocation;
import com.microservice.DefectService.entity.Vehicle;
import com.microservice.DefectService.entity.VehicleDefect;
import com.microservice.DefectService.mapper.ModelMapperService;
import com.microservice.DefectService.repository.DefectLocationRepository;
import com.microservice.DefectService.repository.VehicleDefectRepository;
import com.microservice.DefectService.repository.VehicleRepository;
@Service
public class VehicleDefectManager implements VehicleDefectService {

	@Autowired
	private ModelMapperService modelMapperService;
	
	@Autowired
	private VehicleDefectRepository vehicleDefectRepository;
	
	@Autowired
	private DefectLocationRepository defectLocationRepository;
	
	@Autowired
	private  VehicleRepository vehicleRepository;

	@Override
	public void addDefect(CreateDefectRequest request,String vehicleId) throws IOException {
		
		Vehicle vehicle = vehicleRepository.findByVehicleId(vehicleId);
		
		VehicleDefect defect  = new VehicleDefect();
		defect.setDefectType(request.getDefectType());
		defect.setVehicle(vehicle);
		
		 List<DefectLocation> locations = request.getDefectLocations().stream().map(location -> {
	            DefectLocation defectLocation = new DefectLocation();
	            defectLocation.setX(location.getX());
	            defectLocation.setY(location.getY());
	            defectLocation.setVehicleDefect(defect);
	            return defectLocation;
	        }).collect(Collectors.toList());
	
		 defect.setLocations(locations);

		 vehicleDefectRepository.save(defect);
	
	}


	@Override
	public Page<VehicleDefectResponse> getAllDefects(int no, int size, String sortBy, String sortDirection) {
		Sort.Direction direction = Sort.Direction.fromString(sortDirection);
	    Pageable pageable = PageRequest.of(no, size, direction, sortBy);
	    Page<VehicleDefect> defects = vehicleDefectRepository.findAll(pageable);

	    List<VehicleDefectResponse> responseList = defects.stream()
	    	    .map(defect -> modelMapperService.forResponse().map(defect, VehicleDefectResponse.class))
	    	    .collect(Collectors.toList());
	    return  new PageImpl<>(responseList, pageable, defects.getTotalElements());
	}

	@Override
	public Page<VehicleDefectResponse> getAllDefectsByVehicleId(String vehicleId, int no, int size, String sortBy,
			String sortDirection) {
		Sort.Direction direction = Sort.Direction.fromString(sortDirection);
	    Pageable pageable = PageRequest.of(no, size, direction, sortBy);
	    Page<VehicleDefect> defects = vehicleDefectRepository.findByVehicleId(vehicleId,pageable);
	    
	    List<VehicleDefectResponse> responseList = defects.stream()
	    	    .map(defect -> modelMapperService.forResponse().map(defect, VehicleDefectResponse.class))
	    	    .collect(Collectors.toList());
	    
		return new PageImpl<>(responseList,pageable,defects.getTotalElements());
	} 
	
}
