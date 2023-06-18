package com.microservice.UserService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterBased {

	private String filterCode;
    private int linkCount;
    private String userDesc;
    private String termName;
}
