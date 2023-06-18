package com.microservice.DefectService.business.abstracts;

import java.io.IOException;

import org.springframework.data.domain.Page;

import com.microservice.DefectService.business.requests.CreateDefectRequest;
import com.microservice.DefectService.business.responses.VehicleDefectResponse;

public interface VehicleDefectService {

	/**
	 * Adds a new defect for the specified vehicle.
	 *
	 * @param request   The request object containing the details of the defect to
	 *                  be added.
	 * @param vehicleId The ID of the vehicle for which the defect is being added.
	 * @throws IOException if an error occurs during the process of adding the
	 *                     defect.
	 */
	void addDefect(CreateDefectRequest request, String vehicleId) throws IOException;

	/**
	 * Retrieves a paginated list of all defects.
	 *
	 * @param no            The page number to retrieve.
	 * @param size          The number of items to include per page.
	 * @param sortBy        The field to sort the results by.
	 * @param sortDirection The direction of the sorting (ascending or descending).
	 * @return A Page object containing the requested list of vehicle defects.
	 */
	Page<VehicleDefectResponse> getAllDefects(int no, int size, String sortBy, String sortDirection);


	/**
	 * Retrieves the image data for the specified vehicle.
	 *
	 * @param vehicleId The ID of the vehicle for which to retrieve the image.
	 * @return The byte array representing the image data.
	 * @throws IOException if an error occurs while retrieving the image.
	 */
	byte[] getImage(String vehicleId) throws IOException;

	
	
	

}
