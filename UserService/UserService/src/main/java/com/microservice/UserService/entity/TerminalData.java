package com.microservice.UserService.entity;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "terminals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public  class TerminalData  {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String depName;
    private String depCode;
    private String shopCode;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "terminal_id")
    private List<FilterBased> filterBaseds;
	    
	    
}
