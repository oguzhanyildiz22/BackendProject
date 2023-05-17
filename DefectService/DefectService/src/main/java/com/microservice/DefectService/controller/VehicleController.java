package com.microservice.DefectService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.DefectService.business.abstracts.VehicleService;
import com.microservice.DefectService.business.requests.CreateVehicleRequest;
import com.microservice.DefectService.business.responses.VehicleResponse;

@RestController
@RequestMapping("api/vehicle")
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;
	
	
	@PostMapping("/add")
    public ResponseEntity<String> addVehicle(@RequestBody CreateVehicleRequest request) {
        try {
            vehicleService.add(request);
            return ResponseEntity.ok("Araç başarıyla eklendi.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
	
	
	@GetMapping("/get")
	public ResponseEntity<Page<VehicleResponse>> getVehicles(@RequestParam int no,
			@RequestParam int size,
			@RequestParam String sortBy,
			@RequestParam String sortDirection) {
		Page<VehicleResponse> vehicles = vehicleService.getVehicles(no, size,sortBy,sortDirection);
	 return new ResponseEntity<>(vehicles, HttpStatus.OK);
	}
	
	 @DeleteMapping("/{vehicleId}")
	    public String delete(@PathVariable String vehicleId) {
		 vehicleService.delete(vehicleId);
	        return "deleted";
	    }

}
