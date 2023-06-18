package com.microservice.DefectService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.DefectService.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	Vehicle findByVehicleId(String vehicleId);
	
	boolean existsByVehicleId(String vehicleId);

	void deleteByVehicleId(String vehicleId);
	
	@Query("SELECT v FROM Vehicle v WHERE v.deleted = false")
	Page<Vehicle> findAllActive(Pageable pageable);

}
