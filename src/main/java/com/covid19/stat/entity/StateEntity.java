package com.covid19.stat.entity;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StateEntity {
	
	private List<DistrictEntity> districtData;
	
	private String statename;
	
	private String statecode;
	
	private int confirmed;
	
	private int active;
	
	private int deceased;
	
	private int recovered;
}
