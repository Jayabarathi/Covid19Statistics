package com.covid19.stat.service;

import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import com.covid19.stat.entity.DistrictEntity;
import com.covid19.stat.entity.StateEntity;
import com.covid19.stat.model.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public interface Covid19Service {
	
	public void convertJsonToEntity(String data) throws ParseException, JsonMappingException, JsonProcessingException;
	
	public List<DistrictEntity> getDistrictInfo(Map<String, Object> districtEntity);

	public Map<String, Object> getAllState();

	public Map<String, Object> getAllDistricts(String stateCode);

	public List<Map<String, String>> getStateList();

}
