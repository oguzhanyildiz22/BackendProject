package com.microservice.UserService.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.microservice.UserService.business.abstracts.UserService;
import com.microservice.UserService.business.requests.AddUserRequest;
import com.microservice.UserService.business.requests.UpdateUserRequest;
import com.microservice.UserService.business.responses.UserResponse;

@RestController
@RequestMapping("api/user")
public class UserController {

	@Autowired
	private UserService userService;

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@GetMapping("/admin")
	public String admin(@RequestHeader("Authorization") String authorizationHeader)
			throws JsonMappingException, JsonProcessingException {

		logger.info("Admin endpoint called with authorization header: {}", authorizationHeader);

		if (userService.checkRole(authorizationHeader)) {
			logger.info("User has ADMIN authority");
			return "hello admin";
		} else {
			logger.warn("User does not have ADMIN authority");
			return "You do not have ADMIN authority";
		}
	}

	@PostMapping("/add")
	public String add(@RequestBody AddUserRequest addUserRequest,
			@RequestHeader("Authorization") String authorizationHeader) {

		logger.info("Add endpoint called with authorization header: {}", authorizationHeader);

		if (userService.checkRole(authorizationHeader)) {
			logger.info("User has ADMIN authority. Adding user: {}", addUserRequest);
			this.userService.addUser(addUserRequest);
			return "added";
		} else {
			logger.warn("User does not have ADMIN authority. User not added.");
			return "did not add";
		}

	}

	@PutMapping("/update")
	public String update(@RequestBody UpdateUserRequest updateUserRequest,
			@RequestHeader("Authorization") String authorizationHeader) {

		logger.info("Update endpoint called with authorization header: {}", authorizationHeader);

		if (userService.checkRole(authorizationHeader)) {
			logger.info("User has ADMIN authority. Updating user: {}", updateUserRequest);
			this.userService.updateUser(updateUserRequest);
			return "user updated";
		} else {
			logger.warn("User does not have ADMIN authority. User not updated.");
			return "user did not update";
		}

	}

	@DeleteMapping("/delete/{id}")
	public String softDeleteUserById(@PathVariable("id") int userId,
			@RequestHeader("Authorization") String authorizationHeader) {

		logger.info("Delete endpoint called with authorization header: {}", authorizationHeader);

		if (userService.checkRole(authorizationHeader)) {
			logger.info("User has ADMIN authority. Soft deleting user with ID: {}", userId);
			userService.softDeleteUserById(userId);
			return "deleted";
		} else {
			logger.warn("User does not have ADMIN authority. User not deleted.");
			return "did not delete";
		}
	}

	@GetMapping("/page")
	public ResponseEntity<Page<UserResponse>> getUsers(@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam int no, @RequestParam int size, @RequestParam String sortBy,
			@RequestParam String sortDirection) {

		logger.info("GetUsers endpoint called with authorization header: {}", authorizationHeader);

		if (userService.checkRole(authorizationHeader)) {
			logger.info(
					"User has ADMIN authority. Fetching users with pagination parameters: no={}, size={}, sortBy={}, sortDirection={}",
					no, size, sortBy, sortDirection);
			Page<UserResponse> users = userService.getUsers(no, size, sortBy, sortDirection);
			return new ResponseEntity<>(users, HttpStatus.OK);
		} else {
			logger.warn("User does not have ADMIN authority. Access unauthorized.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
}
