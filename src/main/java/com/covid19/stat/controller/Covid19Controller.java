package com.covid19.stat.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.covid19.stat.service.Covid19Service;

@CrossOrigin(origins = "https://covid19india-statistics.herokuapp.com/")
@Controller
public class Covid19Controller implements ErrorController{

	@Autowired
	Covid19Service covidService;

	public void readJson() throws IOException, Exception {
		URL url = new URL("https://api.covid19india.org/state_district_wise.json");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
		conn.setRequestMethod("GET");
		conn.connect();
		int responsecode = conn.getResponseCode(); 
		String inline = "";
		if(responsecode != 200) {
			throw new RuntimeException("HttpResponseCode " + responsecode);
		} else {
			Scanner sc = new Scanner(url.openStream());
			while(sc.hasNext()) {
				inline += sc.nextLine();
			}
			sc.close();
			covidService.convertJsonToEntity(inline);
		}
	}

	@RequestMapping("/covid19india/states")
	@ResponseBody
	public Map<String, Object> getAllState(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			readJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap = covidService.getAllState();
		return resultMap;
	}

	@GetMapping("/covid19india/districts")
	@ResponseBody
	public Map<String, Object> getAllDistricts(@RequestParam String stateCode){
		Map<String, Object> resultMap = new HashMap<String, Object>(); 
		resultMap = covidService.getAllDistricts(stateCode);
		return resultMap;
	}

	@GetMapping("/covid19india/stateList")
	@ResponseBody
	public List<Map<String, String>> getStateList() {
		List<Map<String, String>> resultList = covidService.getStateList();
		return resultList;
	}

	@Override
	@RequestMapping(value = "/error")
	public String getErrorPath() {
		return "index.html";
	}
}
