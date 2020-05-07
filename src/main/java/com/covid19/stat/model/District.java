package com.covid19.stat.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="district")
@Data
public class District implements Serializable{
	
	@Id
	@Column(name="district_name")
	private String districtName;
	
	private String notes;
	
	private int active;
	
	private int confirmed;
	
	private int deceased;
	
	private int recovered;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="delta_name")
	Delta delta;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="state_code")
	private State state; 

}
