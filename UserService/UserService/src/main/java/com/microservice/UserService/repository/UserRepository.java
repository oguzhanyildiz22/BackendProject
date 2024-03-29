package com.microservice.UserService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservice.UserService.entity.User;

public interface UserRepository extends JpaRepository<User , Integer>{

	@Modifying
	@Query("UPDATE User u SET u.deleted = true WHERE u.id = :userId")
	void softDeleteById(@Param("userId") int userId);
	
	@Query("SELECT u FROM User u WHERE u.deleted = false")
    Page<User> findAllActive(Pageable pageable);

}
