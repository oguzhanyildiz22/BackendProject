package com.microservice.UserService.business.abstracts;

import org.springframework.data.domain.Page;

import com.microservice.UserService.business.requests.AddUserRequest;
import com.microservice.UserService.business.requests.UpdateUserRequest;
import com.microservice.UserService.business.responses.UserResponse;
import com.microservice.UserService.entity.User;

public interface UserService {

	
	String addUser(AddUserRequest addUserRequest);
	String updateUser(UpdateUserRequest updateUserRequest);
	User getUserById(int id);
	void softDeleteUserById(int userId);
	Page<UserResponse> getVehicles(int no, int size, String sortBy, String sortDirection);
}
