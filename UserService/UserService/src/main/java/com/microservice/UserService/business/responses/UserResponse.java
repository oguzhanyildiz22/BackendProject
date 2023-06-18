package com.microservice.UserService.business.responses;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

	private int id;
	
	private String username;
	
	private String password;
	
	private boolean deleted;
	
	private List<RoleResponse> roles = new ArrayList<>();
}
