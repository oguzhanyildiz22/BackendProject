package com.microservice.DefectService.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDefectLocations {

	private int x;
	
	private int y;
}
