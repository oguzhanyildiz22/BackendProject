package com.microservice.UserService.business.concretes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	 

	
	@Override
	public String addUser(AddUserRequest addUserRequest) {
		User user = this.modelMapperService.forRequest()
				    .map(addUserRequest, User.class);
		user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
		
		List<Role> roles = new ArrayList<>();
	    for (String roleName : addUserRequest.getRoles()) {
	        Role role = roleRepository.findByName(roleName);
	        if (role != null) {
	            roles.add(role);
	        } else {
	            return "there is no role you wrote";
	        }
	    }
	    
	    user.setRoles(roles);
		
		this.userRepository.save(user);
		return "create user succesfully";
	}

	@Override
	public String updateUser(UpdateUserRequest updateUserRequest) {
		User user = this.modelMapperService.forRequest()
			    .map(updateUserRequest, User.class);
		user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
		
		List<Role> roles = new ArrayList<>();
	    for (String roleName : updateUserRequest.getRoles()) {
	        Role role = roleRepository.findByName(roleName);
	        if (role != null) {
	            roles.add(role);
	        } else {
	            return "there is no role you wrote";
	        }
	    }
	    
	    user.setRoles(roles);
	
	this.userRepository.save(user);
		return "user updated";
	}

	@Override
	public User getUserById(int id) {
		
		return userRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void softDeleteUserById(int userId) {
		userRepository.softDeleteById(userId);
		
	}

	@Override
	public Page<UserResponse> getVehicles(int no, int size, String sortBy, String sortDirection) {
		Sort.Direction direction = Sort.Direction.fromString(sortDirection);
		Pageable pageable = PageRequest.of(no, size, direction, sortBy);
		Page<User> pageVehicles = userRepository.findAll(pageable);
		List<UserResponse> responseList = pageVehicles.stream()
	    	    .map(defect -> modelMapperService.forResponse().map(defect, UserResponse.class))
	    	    .collect(Collectors.toList());
		return  new PageImpl<>(responseList, pageable, pageVehicles.getTotalElements());
	}

	@Override
	public boolean checkRole(String authorizationHeader) {
		
		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", authorizationHeader);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<String> response = restTemplate.exchange(config.getUrl1(), HttpMethod.GET, entity, String.class);

		String roles = response.getBody();

		if (roles.contains("ADMIN")) {
			return true;
		}
		return false;
	}
	
	 

}
