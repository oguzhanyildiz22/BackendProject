package com.microservice.UserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.UserService.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Integer>{

	Role findByName(String name);

}
