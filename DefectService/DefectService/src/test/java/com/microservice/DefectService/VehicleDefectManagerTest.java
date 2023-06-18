package com.microservice.DefectService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.mock.web.MockMultipartFile;

import com.microservice.DefectService.business.concretes.VehicleDefectManager;
import com.microservice.DefectService.business.requests.CreateDefectLocations;
import com.microservice.DefectService.business.requests.CreateDefectRequest;
import com.microservice.DefectService.business.responses.VehicleDefectResponse;
import com.microservice.DefectService.entity.DefectLocation;
import com.microservice.DefectService.entity.Vehicle;
import com.microservice.DefectService.entity.VehicleDefect;
import com.microservice.DefectService.mapper.ModelMapperService;
import com.microservice.DefectService.repository.VehicleDefectRepository;
import com.microservice.DefectService.repository.VehicleRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class VehicleDefectManagerTest {

	@InjectMocks
	private VehicleDefectManager vehicleDefectManager;
	@Mock
	private ModelMapperService modelMapperService;

	@Mock
	private VehicleDefectRepository vehicleDefectRepository;

	@Mock
	private VehicleRepository vehicleRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddDefect_Success() throws IOException {
		// Arrange
		CreateDefectRequest request = new CreateDefectRequest();
		request.setDefectType("defect");
		MockMultipartFile image = new MockMultipartFile("image", new byte[0]);
		request.setFile(image);

		List<CreateDefectLocations> defectLocations = new ArrayList<>();
		defectLocations.add(new CreateDefectLocations(10, 20));
		defectLocations.add(new CreateDefectLocations(30, 40));
		request.setDefectLocations(defectLocations);
		String vehicleId = "123";

		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleId(vehicleId);

		VehicleDefect vehicleDefect = new VehicleDefect();
		vehicleDefect.setDefectType(request.getDefectType());

		List<DefectLocation> locations = defectLocations.stream().map(location -> {
			DefectLocation defectLocation = new DefectLocation();
			defectLocation.setX(location.getX());
			defectLocation.setY(location.getY());
			defectLocation.setVehicleDefect(vehicleDefect);
			return defectLocation;
		}).collect(Collectors.toList());

		vehicleDefect.setLocations(locations);
		vehicleDefect.setVehicle(vehicle);

		Mockito.when(vehicleRepository.findByVehicleId(vehicleId)).thenReturn(vehicle);
		Mockito.when(vehicleDefectRepository.save(Mockito.any(VehicleDefect.class))).thenReturn(vehicleDefect);

		// Act
		vehicleDefectManager.addDefect(request, vehicleId);

		// Assert
		Mockito.verify(vehicleRepository, Mockito.times(1)).findByVehicleId(Mockito.anyString());
		Mockito.verify(vehicleDefectRepository, Mockito.times(1)).save(Mockito.any(VehicleDefect.class));
	}
	@Test
	public void testAddDefect_Failure_VehicleNotFound() throws IOException {
	    // Arrange
	    CreateDefectRequest request = new CreateDefectRequest();
	    // Set other properties in the request
	    String vehicleId = "123";

	    Mockito.when(vehicleRepository.findByVehicleId(vehicleId)).thenReturn(null); // Simulate a scenario where vehicle is not found

	    // Act and Assert
	    assertThrows(NullPointerException.class, () -> vehicleDefectManager.addDefect(request, vehicleId));

	    Mockito.verify(vehicleRepository, Mockito.times(1)).findByVehicleId(Mockito.anyString());
	    Mockito.verify(vehicleDefectRepository, Mockito.times(0)).save(Mockito.any(VehicleDefect.class));
	}


	@Test
	public void testGetAllDefects() {
		// Arrange
		int no = 0;
		int size = 10;
		String sortBy = "defectType";
		String sortDirection = "ASC";

		List<VehicleDefect> defectList = new ArrayList<>();
		defectList.add(new VehicleDefect());
		defectList.add(new VehicleDefect());
		defectList.add(new VehicleDefect());

		Page<VehicleDefect> defectPage = new PageImpl<>(defectList, PageRequest.of(no, size), defectList.size());

		Mockito.when(modelMapperService.forResponse()).thenReturn(Mockito.mock(ModelMapper.class));
		Mockito.when(vehicleDefectRepository.findAll(Mockito.any(Pageable.class))).thenReturn(defectPage);

		// Act
		Page<VehicleDefectResponse> result = vehicleDefectManager.getAllDefects(no, size, sortBy, sortDirection);

		// Assert
		Assert.assertEquals(defectList.size(), result.getNumberOfElements());
		Assert.assertEquals(size, result.getSize());
		Assert.assertEquals(no, result.getNumber());

		Mockito.verify(vehicleDefectRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));

	}
	@Test
	public void testGetAllDefects_Failure() {
	    // Arrange
	    int no = 0;
	    int size = 10;
	    String sortBy = "vehicleId";
	    String sortDirection = "ASC";

	    Pageable pageable = PageRequest.of(no, size, Sort.Direction.fromString(sortDirection), sortBy);
	    Mockito.when(vehicleDefectRepository.findAll(pageable)).thenThrow(new RuntimeException("Failed to fetch defects"));

	    // Act
	    Throwable exception = assertThrows(RuntimeException.class,
	            () -> vehicleDefectManager.getAllDefects(no, size, sortBy, sortDirection));

	    // Assert
	    assertEquals("Failed to fetch defects", exception.getMessage());
	    Mockito.verify(vehicleDefectRepository, Mockito.times(1)).findAll(pageable);
	}







}
