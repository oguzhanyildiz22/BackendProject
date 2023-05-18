package com.microservice.DefectService.business.abstracts;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestHeader;

import com.microservice.DefectService.business.requests.CreateVehicleRequest;
import com.microservice.DefectService.business.responses.VehicleResponse;

public interface VehicleService {

	/**
	 * Adds a new vehicle based on the provided createVehicleRequest.
	 *
	 * @param createVehicleRequest The request object containing the information of
	 *                             the vehicle to be added.
	 */
	void add(CreateVehicleRequest createVehicleRequest);

	/**
	 * Checks if a vehicle exists based on the provided vehicle ID.
	 *
	 * @param vehicleId The ID of the vehicle to check.
	 * @return {@code true} if a vehicle with the specified ID exists, {@code false}
	 *         otherwise.
	 */
	boolean existByVehicleId(String vehicleId);

	/**
	 * Retrieves a paginated list of vehicles.
	 *
	 * @param no            The page number to retrieve.
	 * @param size          The number of items to include per page.
	 * @param sortBy        The field to sort the results by.
	 * @param sortDirection The direction of the sorting (ascending or descending).
	 * @return A Page object containing the requested list of vehicles.
	 */
	Page<VehicleResponse> getVehicles(int no, int size, String sortBy, String sortDirection);

	/**
	 * Deletes a vehicle by its ID.
	 *
	 * @param vehicleId The ID of the vehicle to delete.
	 */
	void delete(String vehicleId);

	/**
	 * Checks if the user associated with the provided authorization header has the
	 * operator role.
	 *
	 * @param authorizationHeader The Authorization header containing the JWT token.
	 * @return {@code true} if the user has the operator role, {@code false}
	 *         otherwise.
	 */
	boolean checkOperatorRole(@RequestHeader("Authorization") String authorizationHeader);

	/**
	 * Checks if the user associated with the provided authorization header has the
	 * team leader role.
	 *
	 * @param authorizationHeader The Authorization header containing the JWT token.
	 * @return {@code true} if the user has the team leader role, {@code false}
	 *         otherwise.
	 */
	boolean checkTeamLeaderRole(@RequestHeader("Authorization") String authorizationHeader);

}
