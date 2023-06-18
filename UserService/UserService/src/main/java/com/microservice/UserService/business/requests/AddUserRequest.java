package com.microservice.UserService.business.requests;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {

	
	String username;
	String password;
	List<String> roles;
}
