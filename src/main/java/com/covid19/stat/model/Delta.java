package com.covid19.stat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="delta")
@Data
public class Delta {
	
	@Id
	@Column(name = "delta_name")
	private String deltaName;
	
	private int confirmed;
	
	private int deceased;
	
	private int recovered;

}
