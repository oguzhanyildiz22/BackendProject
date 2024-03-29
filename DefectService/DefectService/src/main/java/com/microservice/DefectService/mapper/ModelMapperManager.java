package com.microservice.DefectService.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ModelMapperManager implements ModelMapperService {
	
	private ModelMapper modelMapper;

	public ModelMapperManager() {
		this.modelMapper = new ModelMapper();
	}

	@Override
	public ModelMapper forResponse() {
		this.modelMapper.getConfiguration()
			.setAmbiguityIgnored(true)
			.setMatchingStrategy(MatchingStrategies.LOOSE);
		return this.modelMapper;
	}

	@Override
	public ModelMapper forRequest() {
		this.modelMapper.getConfiguration()
			.setAmbiguityIgnored(true)
			.setMatchingStrategy(MatchingStrategies.LOOSE);
		return this.modelMapper;
	}
}
