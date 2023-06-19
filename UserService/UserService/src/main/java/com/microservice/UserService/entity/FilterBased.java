package com.microservice.UserService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filter_baseds")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterBased {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String filterCode;
	    private int linkCount;
	    private String userDesc;
	    private String termName;
}
