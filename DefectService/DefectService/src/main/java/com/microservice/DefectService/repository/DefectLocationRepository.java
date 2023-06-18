package com.microservice.DefectService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.DefectService.entity.DefectLocation;

@Repository
public interface DefectLocationRepository extends JpaRepository<DefectLocation, Long> {

	List<DefectLocation> findByVehicleDefect_Vehicle_VehicleId(String vehicleId);


}
