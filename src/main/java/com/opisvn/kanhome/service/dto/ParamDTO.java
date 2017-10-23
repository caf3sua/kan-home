package com.opisvn.kanhome.service.dto;

import java.io.Serializable;

public class ParamDTO  implements Serializable {
	
	private static final long serialVersionUID = 7783070045351933235L;
	
	private String name;
	private String unit;
	private String format;
	private String almEn;
	private String almTyp;
	private String normIntv;
	private String almIntv;
	private String redMax;
	private String yelwMax;
	private String yelwMin;
	private String redMin;
	private String errShow;
	private String errSave;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getAlmEn() {
		return almEn;
	}
	public void setAlmEn(String almEn) {
		this.almEn = almEn;
	}
	public String getAlmTyp() {
		return almTyp;
	}
	public void setAlmTyp(String almTyp) {
		this.almTyp = almTyp;
	}
	public String getNormIntv() {
		return normIntv;
	}
	public void setNormIntv(String normIntv) {
		this.normIntv = normIntv;
	}
	public String getAlmIntv() {
		return almIntv;
	}
	public void setAlmIntv(String almIntv) {
		this.almIntv = almIntv;
	}
	public String getRedMax() {
		return redMax;
	}
	public void setRedMax(String redMax) {
		this.redMax = redMax;
	}
	public String getYelwMax() {
		return yelwMax;
	}
	public void setYelwMax(String yelwMax) {
		this.yelwMax = yelwMax;
	}
	public String getYelwMin() {
		return yelwMin;
	}
	public void setYelwMin(String yelwMin) {
		this.yelwMin = yelwMin;
	}
	public String getRedMin() {
		return redMin;
	}
	public void setRedMin(String redMin) {
		this.redMin = redMin;
	}
	public String getErrShow() {
		return errShow;
	}
	public void setErrShow(String errShow) {
		this.errShow = errShow;
	}
	public String getErrSave() {
		return errSave;
	}
	public void setErrSave(String errSave) {
		this.errSave = errSave;
	}
	
	
}
