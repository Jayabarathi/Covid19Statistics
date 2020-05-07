package com.covid19.stat.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.covid19.stat.model.District;
import com.covid19.stat.model.State;

@Component
public interface Covid19Dao {

	public void saveJsonData(List<State> stateModelList);
	
	public List<District> getAllDistricts(String stateCode);

	public List<State> getAllStateInfo();
	
}
