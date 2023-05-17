package com.microservice.DefectService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.DefectService.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	Vehicle findByVehicleId(String vehicleId);
	
	boolean existsByVehicleId(String vehicleId);

	void deleteByVehicleId(String vehicleId);

}
