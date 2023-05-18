package com.microservice.DefectService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.DefectService.entity.Vehicle;
import com.microservice.DefectService.entity.VehicleDefect;

public interface VehicleDefectRepository extends JpaRepository<VehicleDefect, Long> {

	VehicleDefect findByVehicleId(String vehicleId);

	VehicleDefect findByVehicle(Vehicle vehicle);
	
	VehicleDefect findByVehicle_VehicleId(String vehicleId);

	Page<VehicleDefect> findByVehicleId(String vehicleId, Pageable pageable);




}
