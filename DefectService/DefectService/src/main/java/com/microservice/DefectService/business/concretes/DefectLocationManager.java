package com.microservice.DefectService.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.microservice.DefectService.business.abstracts.DefectLocationService;
import com.microservice.DefectService.business.responses.DefectLocationResponse;
import com.microservice.DefectService.entity.DefectLocation;
import com.microservice.DefectService.mapper.ModelMapperService;
import com.microservice.DefectService.repository.DefectLocationRepository;
import com.microservice.DefectService.repository.VehicleDefectRepository;
@Service
public class DefectLocationManager implements DefectLocationService {
	
	@Autowired
	private VehicleDefectRepository vehicleDefectRepository;
	
	@Autowired
	private DefectLocationRepository defectLocationRepository;
	
	@Autowired
	private ModelMapperService modelMapperService;

	@Override
	public Page<DefectLocationResponse> getVehicles(int no, int size, String sortBy, String sortDirection) {
		
		Sort.Direction direction = Sort.Direction.fromString(sortDirection);
	    Pageable pageable = PageRequest.of(no, size, direction, sortBy);
	    Page<DefectLocation> locations = defectLocationRepository.findAll(pageable);

	    List<DefectLocationResponse> responseList = locations.stream()
	    	    .map(defect -> modelMapperService.forResponse().map(defect, DefectLocationResponse.class))
	    	    .collect(Collectors.toList());
	    return  new PageImpl<>(responseList, pageable, locations.getTotalElements());
	}

	
	
}
