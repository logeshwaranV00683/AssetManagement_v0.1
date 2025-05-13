package com.verinite.assetmangementtool.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Getter
@Setter
@Entity
@Table(name="tbl_location")
public class LocationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loc_code",nullable = true)
	//@Pattern(regexp = "^[0-9]{4}$", message = "Contains Pincode only digits")
	private int locCode;
	
	
//	@Pattern(regexp = "^[A-Z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{1,30}$", message = "Start with Captial letter.Location Name must be of 2 to 50 length with no special characters , symbols and digits")
//	@Column(name = "loc_name",nullable = false)	
//	@NotBlank(message = "Location name should not be null")
    private String locName;
	
//	@Pattern(regexp = "^[A-Z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{1,30}$", message = "Start with Captial letter.State must be of 2 to 50 length with no special characters , symbols and digits")
//	@NotBlank(message = "State name should not be null")
	@Column(name = "state",nullable = false)
    private String state;
	
	
//	@Pattern(regexp = "^[A-Z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{1,30}$", message = "Start with Captial letter.Country must be of 2 to 50 length with no special characters , symbols and digits")
//	@NotBlank(message = "Country name should not be null")
	@Column(name = "country",nullable = false)
    private String country;


	public int getLocCode() {
		return locCode;
	}


	public void setLocCode(int locCode) {
		this.locCode = locCode;
	}


	public String getLocName() {
		return locName;
	}


	public void setLocName(String locName) {
		this.locName = locName;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
    
}
