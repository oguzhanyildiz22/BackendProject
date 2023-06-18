package com.microservice.DefectService.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE vehicle_defects SET deleted=true WHERE id=?")
@Table(name = "vehicleDefects")
public class VehicleDefect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "defect_type")
    private String defectType;
    
    @Lob
    private byte[] image;
   

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    
    @JsonIgnore
    @OneToMany(mappedBy = "vehicleDefect",cascade = CascadeType.ALL)
    private List<DefectLocation> locations = new ArrayList<>();
    
    private boolean deleted;
}
