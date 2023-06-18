package com.microservice.UserService.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class MyObject {

	    private String depName;
	    private String depCode;
	    private String shopCode;
	    private List<FilterBased> filterBaseds;
	    
	    
}