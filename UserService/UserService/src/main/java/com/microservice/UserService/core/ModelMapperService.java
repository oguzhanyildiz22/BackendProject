package com.microservice.UserService.core;

import org.modelmapper.ModelMapper;

public interface ModelMapperService {

	ModelMapper forResponse();
	ModelMapper forRequest();
}