package com.covid19.stat.entity;

import lombok.Data;

@Data
public class DistrictEntity {
	
	private String districtName;
	
	private String notes;
	
	private int active;
	
	private int confirmed;
	
	private int deceased;
	
	private int recovered;
	
	DeltaEntity delta;
		
}
