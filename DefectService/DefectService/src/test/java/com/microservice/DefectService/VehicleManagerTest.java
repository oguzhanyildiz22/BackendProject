package com.microservice.DefectService;

import java.util.Collections;
import java.util.List;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.microservice.DefectService.business.concretes.VehicleManager;
import com.microservice.DefectService.business.requests.CreateVehicleRequest;
import com.microservice.DefectService.business.responses.VehicleResponse;
import com.microservice.DefectService.config.RestTemplateConfig;
import com.microservice.DefectService.entity.Vehicle;
import com.microservice.DefectService.mapper.ModelMapperService;
import com.microservice.DefectService.repository.VehicleRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class VehicleManagerTest {

	@InjectMocks
	private VehicleManager vehicleManager;

	@Mock
	private VehicleRepository vehicleRepository;

	@Mock
	private ModelMapperService modelMapperService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private RestTemplateConfig config;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAdd_Success() {
		// Arrange
		CreateVehicleRequest createVehicleRequest = new CreateVehicleRequest();
		createVehicleRequest.setVehicleId("10");

		Mockito.when(vehicleRepository.existsByVehicleId(createVehicleRequest.getVehicleId())).thenReturn(false);
		// Act
		vehicleManager.add(createVehicleRequest);

		// Assert
		Mockito.verify(vehicleRepository, Mockito.times(1)).existsByVehicleId(Mockito.anyString());
		Mockito.verify(vehicleRepository, Mockito.times(1)).save(Mockito.any(Vehicle.class));

	}

	@Test
	public void testAdd_Failure() {
		// Arrange
		CreateVehicleRequest createVehicleRequest = new CreateVehicleRequest();
		createVehicleRequest.setVehicleId("10");

		Mockito.when(vehicleRepository.existsByVehicleId(createVehicleRequest.getVehicleId())).thenReturn(true);
		// Act
		vehicleManager.add(createVehicleRequest);

		// Assert
		Mockito.verify(vehicleRepository, Mockito.times(1)).existsByVehicleId(Mockito.anyString());
	}

//	@Test
//	public void testGetVehicles() {
//		// Arrange
//		int no = 0;
//		int size = 10;
//		String sortBy = "vehicleId";
//		String sortDirection = "ASC";
//
//		Vehicle vehicle = new Vehicle();
//		vehicle.setVehicleId("12");
//		vehicle.setVehicleId("13");
//		List<Vehicle> vehicleList = Collections.singletonList(vehicle);
//
//		Page<Vehicle> pageVehicles = new PageImpl<>(vehicleList);
//
//		Mockito.when(modelMapperService.forResponse()).thenReturn(Mockito.mock(ModelMapper.class));
//		Mockito.when(vehicleRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pageVehicles);
//		Mockito.when(
//				modelMapperService.forResponse().map(Mockito.any(Vehicle.class), Mockito.eq(VehicleResponse.class)))
//				.thenReturn(new VehicleResponse());
//
//		// Act
//		Page<VehicleResponse> result = vehicleManager.getVehicles(no, size, sortBy, sortDirection);
//
//		// Assert
//		Assert.assertEquals(1, result.getContent().size());
//		Mockito.verify(vehicleRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
//		Mockito.verify(modelMapperService.forResponse(), Mockito.times(1)).map(Mockito.any(Vehicle.class),
//				Mockito.eq(VehicleResponse.class));
//
//	}

	@Test 
	public void testDelete_whenExitsVehicle() {
		// Arrange
		String vehicleId = "12";

		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleId("12");

		Mockito.when(vehicleRepository.findByVehicleId(vehicleId)).thenReturn(vehicle);

		// Act
		vehicleManager.delete(vehicleId);

		// Assert
		Mockito.verify(vehicleRepository, Mockito.times(1)).findByVehicleId(Mockito.anyString());
		Mockito.verify(vehicleRepository, Mockito.times(1)).delete(vehicle);
	}

	@Test
	public void testDelete_whenNoExitsVehicle() {
		// Arrange
		String vehicleId = "12";

		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleId("13");

		Mockito.when(vehicleRepository.findByVehicleId(vehicleId)).thenReturn(null);

		// Act
		vehicleManager.delete(vehicleId);

		// Assert
		Mockito.verify(vehicleRepository, Mockito.times(1)).findByVehicleId(Mockito.anyString());
		Mockito.verify(vehicleRepository, Mockito.times(0)).delete(vehicle);
	}

	@Test
	public void testCheckOperatorRole_OperatorRoleExists() {
		// Arrange
		String authorizationHeader = "Bearer token";
		String responseBody = "ROLE_ADMIN, ROLE_OPERATOR";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authorizationHeader);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(Mockito.eq(config.getUrl1()), Mockito.eq(HttpMethod.GET), Mockito.eq(entity),
				Mockito.eq(String.class))).thenReturn(responseEntity);

		// Act
		boolean result = vehicleManager.checkOperatorRole(authorizationHeader);

		// Assert
		Assert.assertTrue(result);
		Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(config.getUrl1()),
				Mockito.eq(HttpMethod.GET), Mockito.eq(entity), Mockito.eq(String.class));
	}

	@Test
	public void testCheckOperatorRole_OperatorRoleNotExists() {
		// Arrange
		String authorizationHeader = "Bearer token";
		String responseBody = "ROLE_ADMIN, ROLE_USER";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authorizationHeader);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(Mockito.eq(config.getUrl1()), Mockito.eq(HttpMethod.GET), Mockito.eq(entity),
				Mockito.eq(String.class))).thenReturn(responseEntity);

		// Act
		boolean result = vehicleManager.checkOperatorRole(authorizationHeader);

		// Assert
		Assert.assertFalse(result);
		Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(config.getUrl1()),
				Mockito.eq(HttpMethod.GET), Mockito.eq(entity), Mockito.eq(String.class));
	}

}
