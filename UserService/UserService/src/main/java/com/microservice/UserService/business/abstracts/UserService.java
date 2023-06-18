package com.microservice.UserService.business.abstracts;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestHeader;

import com.microservice.UserService.business.requests.AddUserRequest;
import com.microservice.UserService.business.requests.UpdateUserRequest;
import com.microservice.UserService.business.responses.UserResponse;
import com.microservice.UserService.entity.User;

public interface UserService {


	/**
	 * Adds a new user based on the provided addUserRequest.
	 *
	 * @param addUserRequest The request object containing the information of the user to be added.
	 * @return A String representing the result or status of the user addition process.
	 */
	String addUser(AddUserRequest addUserRequest);
	
	/**
	 * Updates an existing user based on the provided updateUserRequest.
	 *
	 * @param updateUserRequest The request object containing the updated information of the user.
	 * @return A String representing the result or status of the user update process.
	 */
	String updateUser(UpdateUserRequest updateUserRequest);
	
	/**
	 * Retrieves a user by their ID.
	 *
	 * @param id The ID of the user to retrieve.
	 * @return The User object representing the user with the specified ID, or null if no user is found.
	 */
	User getUserById(int id);
	
	/**
	 * Soft deletes a user by their ID.
	 *
	 * @param userId The ID of the user to be soft deleted.
	 */
	void softDeleteUserById(int userId);
	/**
	 * Retrieves a paginated list of vehicles.
	 *
	 * @param no             The page number to retrieve.
	 * @param size           The number of items to include per page.
	 * @param sortBy         The field to sort the results by.
	 * @param sortDirection  The direction of the sorting (ascending or descending).
	 * @return A Page object containing the requested list of vehicles.
	 */
	Page<UserResponse> getUsers(int no, int size, String sortBy, String sortDirection);
	
	/**
	 * Checks if the user associated with the provided authorization header has the required role.
	 *
	 * @param authorizationHeader The Authorization header containing the JWT token.
	 * @return {@code true} if the user has the required role, {@code false} otherwise.
	 */
	boolean checkRole(@RequestHeader("Authorization") String authorizationHeader);
}
