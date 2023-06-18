package com.microservice.UserService;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.microservice.UserService.business.concretes.UserManager;
import com.microservice.UserService.business.requests.AddUserRequest;
import com.microservice.UserService.business.requests.UpdateUserRequest;
import com.microservice.UserService.business.responses.UserResponse;
import com.microservice.UserService.config.RestTemplateConfig;
import com.microservice.UserService.core.ModelMapperService;
import com.microservice.UserService.entity.Role;
import com.microservice.UserService.entity.User;
import com.microservice.UserService.repository.RoleRepository;
import com.microservice.UserService.repository.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)

public class UserManagerTest {

	@InjectMocks
	private UserManager userManager;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private RestTemplate restTemplate;
	@Mock
	private RestTemplateConfig config;
	@Mock
	private ModelMapperService modelMapperService;
	@Mock
	private UserRepository userRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
    public void testAddUser_Success() {
        // Arrange
        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setUsername("testuser");
        addUserRequest.setPassword("testpassword");
        addUserRequest.setRoles(Arrays.asList("ROLE_USER"));

        User user = new User();
        user.setUsername(addUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        Role role = new Role();
        role.setName("ROLE_USER");
        List<Role> roles = Collections.singletonList(role);
        user.setRoles(roles);

        Mockito.when(modelMapperService.forRequest())
                .thenReturn(Mockito.mock(ModelMapper.class)); 

        Mockito.when(modelMapperService.forRequest().map(addUserRequest, User.class))
                .thenReturn(user);

        Mockito.when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(role);

        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        // Act
        String result = userManager.addUser(addUserRequest);

        // Assert
        Assert.assertEquals("Create user successfully", result);
        Mockito.verify(modelMapperService.forRequest(), Mockito.times(1))
                .map(addUserRequest, User.class);
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByName("ROLE_USER");
        Mockito.verify(userRepository, Mockito.times(1))
                .save(user);
    }
	@Test
	public void testAddUser_Failure() {
	    // Arrange
	    AddUserRequest addUserRequest = new AddUserRequest();
	    addUserRequest.setUsername("testuser");
	    addUserRequest.setPassword("testpassword");
	    addUserRequest.setRoles(Arrays.asList("ROLE_INVALID"));

	    User user = new User();
	    user.setUsername(addUserRequest.getUsername());
	    user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
	    Role role = new Role();
	    role.setName("ROLE_INVALID");
	    List<Role> roles = Collections.singletonList(role);
	    user.setRoles(roles);

	    Mockito.when(modelMapperService.forRequest())
	            .thenReturn(Mockito.mock(ModelMapper.class));

	    Mockito.when(modelMapperService.forRequest().map(addUserRequest, User.class))
	            .thenReturn(user);

	    Mockito.when(roleRepository.findByName("ROLE_INVALID"))
	            .thenReturn(null);

	    // Act
	    String result = userManager.addUser(addUserRequest);

	    // Assert
	    Assert.assertEquals("There is no role you wrote", result);
	    Mockito.verify(modelMapperService.forRequest(), Mockito.times(1))
	            .map(addUserRequest, User.class);
	    Mockito.verify(roleRepository, Mockito.times(1))
	            .findByName("ROLE_INVALID");
	    Mockito.verify(userRepository, Mockito.times(0))
	            .save(user);
	}

	@Test
    public void testGetUserById_Existing() {
        // Arrange
        int userId = 1;
        String username = "testuser";
        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userManager.getUserById(userId);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(userId, result.getId());
        Assert.assertEquals(username, result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    }

    @Test
    public void testGetUserById_NotExisting() {
        // Arrange
        int userId = 1;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userManager.getUserById(userId);

        // Assert
        Assert.assertNull(result);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    }

    
    @Test
    public void testUpdateUser() {
        // Arrange
    	UpdateUserRequest updateUserRequest = new UpdateUserRequest();
    	updateUserRequest.setId(1);
        updateUserRequest.setUsername("testuser");
        updateUserRequest.setPassword("newpassword");
        updateUserRequest.setRoles(Arrays.asList("ROLE_ADMIN"));

        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("testuser");
        existingUser.setPassword("oldpassword");
        Role role = new Role();
        role.setName("ROLE_USER");
        List<Role> roles = Collections.singletonList(role);
        existingUser.setRoles(roles);

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        
       

        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(existingUser));
        Mockito.when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(roleAdmin);
        Mockito.when(passwordEncoder.encode("newpassword")).thenReturn("encodedpassword");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("testuser");
        updatedUser.setPassword("encodedpassword");
        updatedUser.setRoles(Arrays.asList(roleAdmin));
       
        Mockito.when(modelMapperService.forRequest())
        .thenReturn(Mockito.mock(ModelMapper.class)); 
        Mockito.when(modelMapperService.forRequest().map(updateUserRequest, User.class)).thenReturn(updatedUser);

        // Act
        String result = userManager.updateUser(updateUserRequest);

        // Assert
        Assert.assertEquals("User updated successfully", result);
        Mockito.verify(userRepository,Mockito.times(0)).findById(Mockito.anyInt());
        Mockito.verify(roleRepository, Mockito.times(1)).findByName(Mockito.anyString());
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }
    
    @Test
    public void testUpdateUser_Failure() {
        // Arrange
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setId(1);
        updateUserRequest.setUsername("testuser");
        updateUserRequest.setPassword("newpassword");
        updateUserRequest.setRoles(Arrays.asList("ROLE_ADMIN"));

        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("testuser");
        existingUser.setPassword("oldpassword");
        Role role = new Role();
        role.setName("ROLE_USER");
        List<Role> roles = Collections.singletonList(role);
        existingUser.setRoles(roles);

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");

        RuntimeException expectedException = new RuntimeException("Error updating user");

        Mockito.when(modelMapperService.forRequest())
            .thenReturn(Mockito.mock(ModelMapper.class));
        Mockito.when(modelMapperService.forRequest().map(updateUserRequest, User.class)).thenReturn(new User());
        Mockito.when(passwordEncoder.encode(updateUserRequest.getPassword())).thenReturn("encodedPassword");
        Mockito.when(roleRepository.findByName(Mockito.eq("ROLE_ADMIN"))).thenReturn(roleAdmin);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenThrow(expectedException);

        // Act
        String result = userManager.updateUser(updateUserRequest);

        // Assert
        assertEquals("Error updating user", result);

        Mockito.verify(modelMapperService.forRequest(), Mockito.times(1)).map(updateUserRequest, User.class);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(updateUserRequest.getPassword());
        Mockito.verify(roleRepository, Mockito.times(updateUserRequest.getRoles().size())).findByName(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void softDeleteUserById_Successful() {
        // Arrange
        int userId = 1;

       
        doNothing().when(userRepository).softDeleteById(anyInt());

        // Act
        userManager.softDeleteUserById(userId);

        // Assert
        verify(userRepository).softDeleteById(userId);
    }

    @Test
    void softDeleteUserById_UserNotFound() {
        // Arrange
        int userId = 1;
        String errorMessage = "User not found";

        
        doThrow(new RuntimeException(errorMessage)).when(userRepository).softDeleteById(anyInt());

        // Act
        userManager.softDeleteUserById(userId);

        // Assert
        
        verify(userRepository).softDeleteById(userId);
        
    }
    
    @Test
    public void testGetUsers_Success() {
        // Arrange
        int no = 0;
        int size = 10;
        String sortBy = "username";
        String sortDirection = "asc";

        Pageable pageable = PageRequest.of(no, size, Sort.Direction.fromString(sortDirection), sortBy);
        List<User> users = new ArrayList<>(); 
        users.add(new User());
        users.add(new User());
        
        Mockito.when(modelMapperService.forResponse()).thenReturn(Mockito.mock(ModelMapper.class));
        
        Page<User> pageUsers = new PageImpl<>(users, pageable, users.size());
        List<UserResponse> expectedResponseList = users.stream()
                .map(user -> modelMapperService.forResponse().map(user, UserResponse.class))
                .collect(Collectors.toList());
 

        Mockito.when(userRepository.findAllActive(pageable)).thenReturn(pageUsers);

        // Act
        Page<UserResponse> result = userManager.getUsers(no, size, sortBy, sortDirection);

        // Assert
        assertEquals(expectedResponseList, result.getContent());
        assertEquals(pageable, result.getPageable());

        Mockito.verify(userRepository, Mockito.times(1)).findAllActive(pageable);
    }
    
    @Test
    public void testGetUsers_Failure() {
        // Arrange
        int no = 0;
        int size = 10;
        String sortBy = "username";
        String sortDirection = "asc";

        Pageable pageable = PageRequest.of(no, size, Sort.Direction.fromString(sortDirection), sortBy);
        RuntimeException expectedException = new RuntimeException("Failed to fetch users");

        Mockito.when(userRepository.findAllActive(pageable)).thenThrow(expectedException);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> userManager.getUsers(no, size, sortBy, sortDirection));

        Mockito.verify(userRepository, Mockito.times(1)).findAllActive(pageable);
    }


    
    @Test
	public void testCheckAdminRole_AdminRoleExists() {
		// Arrange
		String authorizationHeader = "Bearer token";
		String responseBody = "ROLE_ADMIN, ROLE_TEAM_LEADER";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authorizationHeader);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(Mockito.eq(config.getUrl1()), Mockito.eq(HttpMethod.GET), Mockito.eq(entity),
				Mockito.eq(String.class))).thenReturn(responseEntity);

		// Act
		boolean result = userManager.checkRole(authorizationHeader);

		// Assert
		Assert.assertTrue(result);
		Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(config.getUrl1()),
				Mockito.eq(HttpMethod.GET), Mockito.eq(entity), Mockito.eq(String.class));
	}

	@Test
	public void testCheckAdminRole_AdminRoleNotExists() {
		// Arrange
		String authorizationHeader = "Bearer token";
		String responseBody = "ROLE_OPERATOR, ROLE_USER";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authorizationHeader);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(Mockito.eq(config.getUrl1()), Mockito.eq(HttpMethod.GET), Mockito.eq(entity),
				Mockito.eq(String.class))).thenReturn(responseEntity);

		// Act
		boolean result = userManager.checkRole(authorizationHeader);

		// Assert
		Assert.assertFalse(result);
		Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(config.getUrl1()),
				Mockito.eq(HttpMethod.GET), Mockito.eq(entity), Mockito.eq(String.class));
	}
 
}
