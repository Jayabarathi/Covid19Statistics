package com.covid19.stat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.covid19.stat.dao.Covid19Dao;
import com.covid19.stat.entity.DeltaEntity;
import com.covid19.stat.entity.DistrictEntity;
import com.covid19.stat.entity.StateEntity;
import com.covid19.stat.model.Delta;
import com.covid19.stat.model.District;
import com.covid19.stat.model.State;
import com.covid19.stat.util.KeyConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Covid19ServiceImpl implements Covid19Service{

	@Autowired
	private Covid19Dao daoObject;
	
	private List<StateEntity> totalStateEntityList;
	
	public void convertJsonToEntity(String data) throws ParseException, JsonMappingException, JsonProcessingException{
		List<StateEntity> stateEntityList = new ArrayList<StateEntity>();
		JSONParser parse = new JSONParser();
		JSONObject jObj = (JSONObject) parse.parse(data);
		Set<String> states = jObj.keySet();
 		List<String> stateList = new ArrayList<String>();
 		stateList.addAll(states);
		
		ObjectMapper mapper = new ObjectMapper();
		final HashMap<String, Object> stateInfo = mapper.readValue(data, new TypeReference<HashMap>() {});
		stateEntityList = stateList.stream().map(state -> {
			Map<String, Object> stateEntity = (HashMap<String, Object>) stateInfo.get(state);
			StateEntity entityObj = new StateEntity();
			entityObj.setStatecode(stateEntity.get(KeyConstants.STATECODE).toString());
			entityObj.setStatename(state);
			List<DistrictEntity> districts = getDistrictInfo((Map<String, Object>) stateEntity.get(KeyConstants.DISTRICTDATA));
			entityObj.setDistrictData(districts);
			return entityObj;
		}).collect(Collectors.toList());
		List<State> stateModelList = convertEntityToModel(stateEntityList);
		daoObject.saveJsonData(stateModelList);
	}
	
	private List<State> convertEntityToModel(List<StateEntity> stateEntityList) {
		List<State> stateList = new ArrayList<State>();
		stateList = stateEntityList.stream().map(state -> {
			State stateInfo = new State();
			stateInfo.setStatecode(state.getStatecode());
			stateInfo.setStateName(state.getStatename());
			List<District> districtData = getDistrictModel(stateInfo, state.getDistrictData());
			stateInfo.setDistrictData(districtData);
			return stateInfo;
		}).collect(Collectors.toList());
		return stateList;
	}

	private List<District> getDistrictModel(State state, List<DistrictEntity> districtData) {
		List<District> districtList = new ArrayList<District>();
		districtList = districtData.stream().map(district -> {
			District districtInfo = new District();
			districtInfo.setActive(district.getActive());
			districtInfo.setConfirmed(district.getConfirmed());
			districtInfo.setDeceased(district.getDeceased());
			districtInfo.setDistrictName(district.getDistrictName());
			districtInfo.setNotes(district.getNotes());
			districtInfo.setRecovered(district.getRecovered());
			districtInfo.setState(state);
			Delta delta = getDelta(state.getStatecode()+ "_" + district.getDistrictName(), district.getDelta());
			districtInfo.setDelta(delta);
			return districtInfo;
		}).collect(Collectors.toList());
		return districtList;
	}

	private Delta getDelta(String deltaName, DeltaEntity deltaEntity) {
		Delta delta = new Delta();
		delta.setDeltaName(deltaName);
		delta.setConfirmed(deltaEntity.getConfirmed());
		delta.setDeceased(deltaEntity.getDeceased());
		delta.setRecovered(deltaEntity.getRecovered());
		return delta;
	}

	public List<DistrictEntity> getDistrictInfo(Map<String, Object> districtEntity){
		List<DistrictEntity> districts = new ArrayList<DistrictEntity>();
		Set<String> keySet = districtEntity.keySet();
		List<String> districtNameList = new ArrayList<String>();
		districtNameList.addAll(keySet);
		districts = districtNameList.stream().map(district -> {
			DistrictEntity entity = new DistrictEntity();
			Map<String, Object> districtMap = (Map<String, Object>) districtEntity.get(district);
			entity.setDistrictName(district);
			entity.setConfirmed(Integer.parseInt(districtMap.get(KeyConstants.CONFIRMED).toString()));
			entity.setDeceased(Integer.parseInt(districtMap.get(KeyConstants.DECEASED).toString()));
			entity.setNotes(districtMap.get(KeyConstants.NOTES).toString());
			entity.setRecovered(Integer.parseInt(districtMap.get(KeyConstants.RECOVERED).toString()));
			if((district.equals("Unknown")) || (entity.getConfirmed() - (entity.getDeceased()+entity.getRecovered())) < 0) {
				entity.setActive(Integer.parseInt(districtMap.get(KeyConstants.ACTIVE).toString()));
			} else {
				entity.setActive(entity.getConfirmed() - (entity.getDeceased()+entity.getRecovered()));
			}
			DeltaEntity delta = new DeltaEntity();
			Map<String, Object> deltaMap = (Map<String, Object>) districtMap.get(KeyConstants.DELTA);
			delta.setConfirmed(Integer.parseInt(deltaMap.get(KeyConstants.CONFIRMED).toString()));
			delta.setDeceased(Integer.parseInt(deltaMap.get(KeyConstants.DECEASED).toString()));
			delta.setRecovered(Integer.parseInt(deltaMap.get(KeyConstants.RECOVERED).toString()));
			entity.setDelta(delta);
			return entity;
		}).collect(Collectors.toList());
		return districts;
	}

	@Override
	public Map<String, Object> getAllState() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<StateEntity> stateEntityList = new ArrayList<StateEntity>();
		List<State> stateList = daoObject.getAllStateInfo();
		AtomicInteger countryTotalConfirmed = new AtomicInteger(0);
		AtomicInteger countryTotalActive = new AtomicInteger(0);
		AtomicInteger countryTotalDeceased = new AtomicInteger(0);
		AtomicInteger countryTotalRecovered = new AtomicInteger(0);
		stateEntityList = stateList.stream().map(state -> {
			StateEntity entity = new StateEntity();
			entity.setStatecode(state.getStatecode());
			entity.setStatename(state.getStateName());
			findStateTotal(entity, state.getDistrictData());
			countryTotalConfirmed.addAndGet(entity.getConfirmed());
			countryTotalDeceased.addAndGet(entity.getDeceased());
			countryTotalRecovered.addAndGet(entity.getRecovered());
			countryTotalActive.addAndGet(entity.getConfirmed() - (entity.getDeceased()+entity.getRecovered()));
			return entity;
		}).collect(Collectors.toList());
		resultMap.put("countryName", "INDIA");
		resultMap.put("countryActive", countryTotalActive.intValue());
		resultMap.put("countryConfirmed", countryTotalConfirmed.intValue());
		resultMap.put("countryDeceased", countryTotalDeceased.intValue());
		resultMap.put("countryRecovered", countryTotalRecovered.intValue());
		resultMap.put("stateList", stateEntityList);
		totalStateEntityList = stateEntityList;
		return resultMap;
	}

	private void findStateTotal(StateEntity entity, List<District> districtData) {
		AtomicInteger totalConfirmed = new AtomicInteger(0);
		AtomicInteger totalDeceased = new AtomicInteger(0);
		AtomicInteger totalRecovered = new AtomicInteger(0);
		districtData.stream().forEach(district -> {
			totalConfirmed.addAndGet(district.getConfirmed());
			totalDeceased.addAndGet(district.getDeceased());
			totalRecovered.addAndGet(district.getRecovered());
		});
		entity.setConfirmed(totalConfirmed.intValue());
		entity.setDeceased(totalDeceased.intValue());
		entity.setRecovered(totalRecovered.intValue());
		entity.setActive(entity.getConfirmed() - (entity.getDeceased()+entity.getRecovered()));
	}

	@Override
	public Map<String, Object> getAllDistricts(String stateCode) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<District> districtList = daoObject.getAllDistricts(stateCode);
		List<DistrictEntity> districtEntityList = new ArrayList<DistrictEntity>();
		AtomicInteger stateTotalConfirmed = new AtomicInteger(0);
		AtomicInteger stateTotalActive = new AtomicInteger(0);
		AtomicInteger stateTotalDeceased = new AtomicInteger(0);
		AtomicInteger stateTotalRecovered = new AtomicInteger(0);
		districtEntityList = districtList.stream().map(district -> {
			DistrictEntity entity = new DistrictEntity();
			entity.setActive(district.getActive());
			entity.setConfirmed(district.getConfirmed());
			entity.setDeceased(district.getDeceased());
			entity.setDistrictName(district.getDistrictName());
			entity.setNotes(district.getNotes());
			entity.setRecovered(district.getRecovered());
			stateTotalConfirmed.addAndGet(entity.getConfirmed());
			stateTotalDeceased.addAndGet(entity.getDeceased());
			stateTotalRecovered.addAndGet(entity.getRecovered());
			stateTotalActive.addAndGet(entity.getConfirmed() - (entity.getDeceased()+entity.getRecovered()));
			DeltaEntity deltaEntity = getDeltaEntity(district.getDelta());
			entity.setDelta(deltaEntity);
			return entity;
		}).collect(Collectors.toList());
		resultMap.put("districtList", districtEntityList);
		resultMap.put("stateConfirmed", stateTotalConfirmed.intValue());
		resultMap.put("stateDeceased", stateTotalDeceased.intValue());
		resultMap.put("stateRecovered", stateTotalRecovered.intValue());
		resultMap.put("stateActive", stateTotalActive.intValue());
		resultMap.put("stateList",totalStateEntityList);
		return resultMap;
	}

	private DeltaEntity getDeltaEntity(Delta delta) {
		DeltaEntity entity = new DeltaEntity();
		entity.setConfirmed(entity.getConfirmed());
		entity.setDeceased(entity.getDeceased());
		entity.setRecovered(entity.getRecovered());
		return entity;
	}

	@Override
	public List<Map<String, String>> getStateList() {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<State> stateList = daoObject.getAllStateInfo();
		resultList = stateList.stream().map(state -> {
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("statecode", state.getStatecode());
			resultMap.put("statename", state.getStateName());
			return resultMap;
		}).collect(Collectors.toList());
		return resultList;
	}

}
