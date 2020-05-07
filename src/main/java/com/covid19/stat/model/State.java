package com.covid19.stat.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name="state")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"updatedAt"}, 
allowGetters = true)
@Data
public class State implements Serializable{
	
	@Id
	@Column(name="state_code")
	private String statecode;
	
	@NotBlank
	@Column(name="state_name")
	private String stateName;
	
	@OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
	private List<District> districtData;
	
	@Column(name="updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

}
