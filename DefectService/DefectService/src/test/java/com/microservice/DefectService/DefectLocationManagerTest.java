package com.microservice.DefectService;

import java.util.ArrayList;
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

import com.microservice.DefectService.business.concretes.DefectLocationManager;
import com.microservice.DefectService.business.responses.DefectLocationResponse;
import com.microservice.DefectService.entity.DefectLocation;
import com.microservice.DefectService.mapper.ModelMapperService;
import com.microservice.DefectService.repository.DefectLocationRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class DefectLocationManagerTest {

	@InjectMocks
	private DefectLocationManager defectLocationManager;

	@Mock
	private DefectLocationRepository defectLocationRepository;

	@Mock
	private ModelMapperService modelMapperService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetVehicles() {
		// Arrange
		int no = 0;
		int size = 10;
		String sortBy = "defectType";
		String sortDirection = "asc";

		List<DefectLocation> defectLocations = new ArrayList<>();
		defectLocations.add(new DefectLocation());
		defectLocations.add(new DefectLocation());

		Page<DefectLocation> locations = new PageImpl<>(defectLocations);

		Mockito.when(modelMapperService.forResponse()).thenReturn(Mockito.mock(ModelMapper.class));

		Mockito.when(defectLocationRepository.findAll(Mockito.any(Pageable.class))).thenReturn(locations);

		// Act
		Page<DefectLocationResponse> result = defectLocationManager.getVehicles(no, size, sortBy, sortDirection);

		// Assert
		Assert.assertEquals(defectLocations.size(), result.getContent().size());
		Mockito.verify(defectLocationRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
	}

}
