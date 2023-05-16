package com.microservice.UserService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;

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
	private RestTemplate restTemplate;

	private String url1 = "http://localhost:8000/api/auth/getRole";
	@Autowired
	private UserService userService;

	@GetMapping("/admin")
	public String admin(@RequestHeader("Authorization") String authorizationHeader)
			throws JsonMappingException, JsonProcessingException {

		if (checkRole(authorizationHeader)) {
			return "hello admin";
		} else {
			return "You do not have ADMIN authority";
		}
	}

	// check role by using authorizationHeader
	public boolean checkRole(@RequestHeader("Authorization") String authorizationHeader) {

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", authorizationHeader);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<String> response = restTemplate.exchange(url1, HttpMethod.GET, entity, String.class);

		String roles = response.getBody();

		if (roles.contains("ADMIN")) {
			return true;
		}
		return false;
	}
	@PostMapping("/add")
	public String add(@RequestBody AddUserRequest addUserRequest,@RequestHeader("Authorization") String authorizationHeader) {
		
		if(checkRole(authorizationHeader)) {
			this.userService.addUser(addUserRequest);
			
			return "added";
		}
		return "did not add";
		
	}
	@PutMapping("/update")
	public String update(@RequestBody UpdateUserRequest updateUserRequest,@RequestHeader("Authorization") String authorizationHeader) {
		 
		if (checkRole(authorizationHeader)) {
			this.userService.updateUser(updateUserRequest);
			return "user updated";
		}
		
		return "user did not update";
		
	}
	
	@DeleteMapping("/delete/{id}")
    public String softDeleteUserById(@PathVariable("id") int userId,@RequestHeader("Authorization") String authorizationHeader) {
       
		if (checkRole(authorizationHeader)) {
			userService.softDeleteUserById(userId);
	        return "deleted";
		}
		return "did not deleted";
    }
	
	@GetMapping("/page")
	public ResponseEntity<Page<UserResponse>> getUsers(@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam int no,
			@RequestParam int size,
			@RequestParam String sortBy,
			@RequestParam String sortDirection){
		if (checkRole(authorizationHeader)) {
			Page<UserResponse> users = userService.getVehicles(no, size,sortBy,sortDirection);
			return new ResponseEntity<>(users, HttpStatus.OK);
		}
		
		return new ResponseEntity<Page<UserResponse>>(HttpStatus.UNAUTHORIZED);
	}
}
