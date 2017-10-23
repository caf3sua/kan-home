package com.opisvn.kanhome.service.dto;

import java.io.Serializable;

public class FilterDTO implements Serializable {

	private static final long serialVersionUID = 4864394239011756109L;
    	
	private String name;
    private String fullname;
    private String feature;
    private Double value;
	private String unit;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
