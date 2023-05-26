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
import org.springframework.stereotype.Service;

import com.microservice.DefectService.business.abstracts.DefectLocationService;
import com.microservice.DefectService.business.responses.DefectLocationResponse;
import com.microservice.DefectService.entity.DefectLocation;
import com.microservice.DefectService.mapper.ModelMapperService;
import com.microservice.DefectService.repository.DefectLocationRepository;

@Service
public class DefectLocationManager implements DefectLocationService {

	@Autowired
	private DefectLocationRepository defectLocationRepository;

	@Autowired
	private ModelMapperService modelMapperService;

	private static final Logger logger = LogManager.getLogger(DefectLocationManager.class);
	
	

//	@Override
//	public Page<DefectLocationResponse> getVehicles(int no, int size, String sortBy, String sortDirection) {
//		
//		Sort.Direction direction = Sort.Direction.fromString(sortDirection);
//	    Pageable pageable = PageRequest.of(no, size, direction, sortBy);
//	    Page<DefectLocation> locations = defectLocationRepository.findAll(pageable);
//
//	    List<DefectLocationResponse> responseList = locations.stream()
//	    	    .map(defect -> modelMapperService.forResponse().map(defect, DefectLocationResponse.class))
//	    	    .collect(Collectors.toList());
//	    return  new PageImpl<>(responseList, pageable, locations.getTotalElements());
//	}

	@Override
	public Page<DefectLocationResponse> getVehicles(int no, int size, String sortBy, String sortDirection) {
		Sort.Direction direction = Sort.Direction.fromString(sortDirection);
		Pageable pageable = PageRequest.of(no, size, direction, sortBy);
		Page<DefectLocation> locations = defectLocationRepository.findAll(pageable);

		List<DefectLocationResponse> responseList = locations.stream()
				.map(defect -> modelMapperService.forResponse().map(defect, DefectLocationResponse.class))
				.collect(Collectors.toList());

		logger.info("Retrieved {} Defect Location(s) with no: {}, size: {}, sortBy: {}, sortDirection: {}",
				locations.getTotalElements(), no, size, sortBy, sortDirection);

		return new PageImpl<>(responseList, pageable, locations.getTotalElements());
	}

}
