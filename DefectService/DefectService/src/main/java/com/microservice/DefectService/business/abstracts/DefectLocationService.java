package com.microservice.DefectService.business.abstracts;

import org.springframework.data.domain.Page;

import com.microservice.DefectService.business.responses.DefectLocationResponse;

public interface DefectLocationService {

	/**
	 * Retrieves a paginated list of vehicles with defect location information.
	 *
	 * @param no             The page number to retrieve.
	 * @param size           The number of items to include per page.
	 * @param sortBy         The field to sort the results by.
	 * @param sortDirection  The direction of the sorting (ascending or descending).
	 * @return A Page object containing the requested list of vehicles with defect location information.
	 */
	Page<DefectLocationResponse> getVehicles(int no, int size, String sortBy, String sortDirection);

}
