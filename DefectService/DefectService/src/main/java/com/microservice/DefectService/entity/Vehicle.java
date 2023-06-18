package com.microservice.DefectService.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE vehicles SET deleted=true WHERE id=?")
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "vehicle_id")
    private String vehicleId;
    
    @JsonIgnore
    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.REMOVE)
    private List<VehicleDefect> defects = new ArrayList<>();
    
    
    
    private boolean deleted;
}
