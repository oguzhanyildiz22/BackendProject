package com.microservice.UserService.business.concretes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.UserService.business.abstracts.UserService;
import com.microservice.UserService.business.requests.AddUserRequest;
import com.microservice.UserService.business.requests.UpdateUserRequest;
import com.microservice.UserService.business.responses.UserResponse;
import com.microservice.UserService.config.RestTemplateConfig;
import com.microservice.UserService.core.ModelMapperService;
import com.microservice.UserService.entity.Role;
import com.microservice.UserService.entity.User;
import com.microservice.UserService.repository.RoleRepository;
import com.microservice.UserService.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserManager implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ModelMapperService modelMapperService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RestTemplateConfig config;
	
	
	
	 
	private static final Logger logger = LogManager.getLogger(UserManager.class);

	@Override
	public String addUser(AddUserRequest addUserRequest) {
		try {
	        logger.info("Adding user: {}", addUserRequest.getUsername());

	        User user = this.modelMapperService.forRequest().map(addUserRequest, User.class);
	        user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));

	        List<Role> roles = new ArrayList<>();
	        for (String roleName : addUserRequest.getRoles()) {
	            Role role = roleRepository.findByName(roleName);
	            if (role != null) {
	                roles.add(role);
	            } else {
	                logger.warn("Invalid role: {}", roleName);
	                return "There is no role you wrote";
	            }
	        }

	        user.setRoles(roles);

	        this.userRepository.save(user);

	        logger.info("User created successfully: {}", addUserRequest.getUsername());
	        return "Create user successfully";
	    } catch (Exception e) {
	        logger.error("Error adding user: {}", e.getMessage());
	        return "Error adding user";
	    }
	}

	@Override
	public String updateUser(UpdateUserRequest updateUserRequest) {
		try {
	        logger.info("Updating user: {}", updateUserRequest.getUsername());

	        User user = this.modelMapperService.forRequest().map(updateUserRequest, User.class);
	        user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));

	        List<Role> roles = new ArrayList<>();
	        for (String roleName : updateUserRequest.getRoles()) {
	            Role role = roleRepository.findByName(roleName);
	            if (role != null) {
	                roles.add(role);
	            } else {
	                logger.warn("Invalid role: {}", roleName);
	                return "There is no role you wrote";
	            }
	        }

	        user.setRoles(roles);

	        this.userRepository.save(user);

	        logger.info("User updated successfully: {}", updateUserRequest.getUsername());
	        return "User updated successfully";
	    } catch (Exception e) {
	        logger.error("Error updating user: {}", e.getMessage());
	        return "Error updating user";
	    }
	}

	@Override
	public User getUserById(int id) {
		 try {
		        logger.info("Getting user by ID: {}", id);

		        User user = userRepository.findById(id).orElse(null);

		        if (user != null) {
		            logger.info("User found: {}", user.getUsername());
		        } else {
		            logger.warn("User not found with ID: {}", id);
		        }

		        return user;
		    } catch (Exception e) {
		        logger.error("Error getting user by ID: {}", e.getMessage());
		        return null;
		    }
	}

	@Override
	@Transactional
	public void softDeleteUserById(int userId) {
		 try {
		        logger.info("Soft deleting user with ID: {}", userId);

		        userRepository.softDeleteById(userId);

		        logger.info("User soft deleted successfully");
		    } catch (Exception e) {
		        logger.error("Error occurred while soft deleting user: {}", e.getMessage());
		    }
		
	}

	@Override
	public Page<UserResponse> getUsers(int no, int size, String sortBy, String sortDirection) {
		try {
	        logger.info("Fetching users with pagination - Page: {}, Size: {}, SortBy: {}, SortDirection: {}",
	                no, size, sortBy, sortDirection);

	        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
	        Pageable pageable = PageRequest.of(no, size, direction, sortBy);
	        Page<User> pageVehicles = userRepository.findAll(pageable);
	        List<UserResponse> responseList = pageVehicles.stream()
	                .map(user -> modelMapperService.forResponse().map(user, UserResponse.class))
	                .collect(Collectors.toList());

	        logger.info("Fetched users successfully");

	        return new PageImpl<>(responseList, pageable, pageVehicles.getTotalElements());
	    } catch (Exception e) {
	        logger.error("Error occurred while fetching users: {}", e.getMessage());
	        throw new RuntimeException("Failed to fetch users");
	    }
	}

	@Override
	public boolean checkRole(String authorizationHeader) {
		
		try {
	        logger.info("Checking user roles with authorization header: {}", authorizationHeader);

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", authorizationHeader);
	        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

	        ResponseEntity<String> response = restTemplate.exchange(config.getUrl1(), HttpMethod.GET, entity, String.class);

	        String roles = response.getBody();

	        if (roles.contains("ADMIN")) {
	            logger.info("User has the role 'ADMIN'");
	            return true;
	        }

	        logger.info("User does not have the role 'ADMIN'");
	        return false;
	    } catch (Exception e) {
	        logger.error("Error occurred while checking user roles: {}", e.getMessage());
	        throw new RuntimeException("Failed to check user roles");
	    }
	}
	
	 

}
