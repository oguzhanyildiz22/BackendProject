package com.microservice.DefectService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.DefectService.entity.ImageData;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData,Long> {

	

	ImageData findByVehicleId(String vehicleId);

}
