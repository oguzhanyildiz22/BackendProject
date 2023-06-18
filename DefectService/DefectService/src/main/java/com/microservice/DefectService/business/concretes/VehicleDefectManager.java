package com.microservice.DefectService.business.concretes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.microservice.DefectService.business.abstracts.VehicleDefectService;
import com.microservice.DefectService.business.requests.CreateDefectRequest;
import com.microservice.DefectService.business.responses.VehicleDefectResponse;
import com.microservice.DefectService.entity.DefectLocation;
import com.microservice.DefectService.entity.Vehicle;
import com.microservice.DefectService.entity.VehicleDefect;
import com.microservice.DefectService.mapper.ModelMapperService;
import com.microservice.DefectService.repository.VehicleDefectRepository;
import com.microservice.DefectService.repository.VehicleRepository;

import jakarta.transaction.Transactional;

@Service
public class VehicleDefectManager implements VehicleDefectService {

	@Autowired
	private ModelMapperService modelMapperService;

	@Autowired
	private VehicleDefectRepository vehicleDefectRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	private static final Logger logger = LogManager.getLogger(VehicleDefectManager.class);

	@Override
	public void addDefect(CreateDefectRequest request, String vehicleId) throws IOException {
		
		logger.info("Begin addDefect method to add vehicle's defect with vehicleId: {}",vehicleId);
		try {
			Vehicle vehicle = vehicleRepository.findByVehicleId(vehicleId);

			VehicleDefect defect = new VehicleDefect();
			defect.setDefectType(request.getDefectType());
			defect.setImage(request.getFile().getBytes());
			defect.setVehicle(vehicle);

			List<DefectLocation> locations = request.getDefectLocations().stream().map(location -> {
				DefectLocation defectLocation = new DefectLocation();
				defectLocation.setX(location.getX());
				defectLocation.setY(location.getY());
				defectLocation.setVehicleDefect(defect);
				return defectLocation;
			}).collect(Collectors.toList());

			defect.setLocations(locations);

			vehicleDefectRepository.save(defect);

			logger.info("Defect added successfully: defectId={}, vehicleId={}", defect.getId(), vehicleId);
		} catch (Exception e) {
			logger.error("Failed to add defect for vehicleId={}. Error: {}", vehicleId, e.getMessage());
			throw e;
		}
	}

	@Override
	public Page<VehicleDefectResponse> getAllDefects(int no, int size, String sortBy, String sortDirection) {
		logger.info("Begin getAllDefects method to Fetch vehicles with pagination - Page: {}, Size: {}, SortBy: {}, SortDirection: {}",
	            no, size, sortBy, sortDirection);
		Sort.Direction direction = Sort.Direction.fromString(sortDirection);
		Pageable pageable = PageRequest.of(no, size, direction, sortBy);
		Page<VehicleDefect> defects = vehicleDefectRepository.findAll(pageable);

		List<VehicleDefectResponse> responseList = defects.stream()
				.map(defect -> modelMapperService.forResponse().map(defect, VehicleDefectResponse.class))
				.collect(Collectors.toList());

		long totalElements = defects.getTotalElements();
		int totalPages = defects.getTotalPages();

		logger.info("Retrieved {} defects. Page {}/{}", totalElements, defects.getNumber() + 1, totalPages);

		return new PageImpl<>(responseList, pageable, totalElements);
	}

	@Override
	@Transactional
	public byte[] getImage(String vehicleId) throws IOException {
		
		logger.info("Begin getImage method to get and mark  vehicle's image with vehicleId: {}",vehicleId);

		VehicleDefect vehicleDefect = vehicleDefectRepository.findByVehicle_VehicleId(vehicleId);
		byte[] imageData = vehicleDefect.getImage();
		List<DefectLocation> defectLocations = vehicleDefect.getLocations();

		InputStream inputStream = new ByteArrayInputStream(imageData);
		BufferedImage bufferedImage = ImageIO.read(inputStream);

		Graphics2D graphics = bufferedImage.createGraphics();
		graphics.setColor(Color.WHITE);
		

		for (DefectLocation location : defectLocations) {
			int x = location.getX();
			int y = location.getY();

			int rectSize = 40; // Dikdörtgen boyutu
			int triangleSize = 20; // Üçgen boyutu

			// Dikdörtgen çizimi
			graphics.drawRect(x - rectSize / 2, y - rectSize / 2, rectSize, rectSize);

			// Üçgen çizimi
			int[] xPoints = { x - triangleSize / 2, x, x + triangleSize / 2 };
			int[] yPoints = { y + rectSize / 2, y - rectSize / 2, y + rectSize / 2 };
			graphics.fillPolygon(xPoints, yPoints, 3);

			logger.info("Marked defect location at x: {}, y: {}", x, y);
		}

		graphics.dispose();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", outputStream);

		logger.info("Generated defect image for vehicleId: {}", vehicleId);

		return outputStream.toByteArray();
	}

}
