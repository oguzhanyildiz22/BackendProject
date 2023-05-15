package com.microservice.UserService.business.concretes;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.microservice.UserService.business.abstracts.UserService;
import com.microservice.UserService.business.requests.AddUserRequest;
import com.microservice.UserService.business.requests.UpdateUserRequest;
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
	
	 

}
