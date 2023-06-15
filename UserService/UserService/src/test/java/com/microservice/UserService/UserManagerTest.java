package com.microservice.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.Pageable;
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
    public void testSoftDeleteUserById() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userManager.softDeleteUserById(userId);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).softDeleteById(userId);
    }
//    @Test
//    public void testGetUsers() {
//        // Arrange
//        int page = 0;
//        int size = 10;
//        String sortBy = "username";
//        String sortDirection = "asc";
//
//        User user1 = new User();
//        user1.setId(1);
//        user1.setUsername("user1");
//
//        User user2 = new User();
//        user2.setId(2);
//        user2.setUsername("user2");
//
//        List<User> userList = Arrays.asList(user1, user2);
//        Page<User> userPage = new PageImpl<>(userList);
//        
//        Mockito.when(modelMapperService.forResponse())
//        .thenReturn(Mockito.mock(ModelMapper.class));
//
//        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class)))
//                .thenReturn(userPage);
//
//        UserResponse userResponse1 = new UserResponse();
//        userResponse1.setId(1);
//        userResponse1.setUsername("user1");
//
//        UserResponse userResponse2 = new UserResponse();
//        userResponse2.setId(2);
//        userResponse2.setUsername("user2");
//
//        Mockito.when(modelMapperService.forResponse().map(Mockito.any(User.class), Mockito.eq(UserResponse.class)))
//                .thenReturn(userResponse1, userResponse2);
//
//        // Act
//        Page<UserResponse> result = userManager.getUsers(page, size, sortBy, sortDirection);
//
//        // Assert
//        Assert.assertEquals(userList.size(), result.getContent().size());
//        Assert.assertEquals(userList.get(0).getUsername(), result.getContent().get(0).getUsername());
//        Assert.assertEquals(userList.get(1).getUsername(), result.getContent().get(1).getUsername());
//
//        Mockito.verify(userRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
//        Mockito.verify(modelMapperService.forResponse(), Mockito.times(2))
//                .map(Mockito.any(User.class), Mockito.eq(UserResponse.class));
//    }
    
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
 
}
