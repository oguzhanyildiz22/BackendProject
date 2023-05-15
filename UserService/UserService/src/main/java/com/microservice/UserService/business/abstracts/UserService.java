package com.microservice.UserService.business.abstracts;

import com.microservice.UserService.business.requests.AddUserRequest;
import com.microservice.UserService.business.requests.UpdateUserRequest;
import com.microservice.UserService.entity.User;

public interface UserService {

	
	String addUser(AddUserRequest addUserRequest);
	String updateUser(UpdateUserRequest updateUserRequest);
	User getUserById(int id);
	void softDeleteUserById(int userId);
}
