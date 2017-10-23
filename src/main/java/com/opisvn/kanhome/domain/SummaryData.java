package com.opisvn.kanhome.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A summary.
 */

@Document(collection = "summary_data")
public class SummaryData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    // User
    @Field("number_user")
    private Long numberUser;
    
    @Field("number_admin_user")
    private Long numberAdminUser;
    
    @Field("number_technical_user")
    private Long numberTechnicalUser;
    
    @Field("number_supportor_user")
    private Long numberSupportorUser;
    
    // Device
    @Field("number_device")
    private Long numberDevice;
    
    @Field("number_blue_device")
    private Long numberBlueDevice;
    
    @Field("number_yellow_device")
    private Long numberYellowDevice;
    
    @Field("number_red_device")
    private Long numberRedDevice;
    
    @Field("number_gray_device")
    private Long numberGrayDevice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumberUser() {
		return numberUser;
	}

	public void setNumberUser(Long numberUser) {
		this.numberUser = numberUser;
	}

	public Long getNumberAdminUser() {
		return numberAdminUser;
	}

	public void setNumberAdminUser(Long numberAdminUser) {
		this.numberAdminUser = numberAdminUser;
	}

	public Long getNumberTechnicalUser() {
		return numberTechnicalUser;
	}

	public void setNumberTechnicalUser(Long numberTechnicalUser) {
		this.numberTechnicalUser = numberTechnicalUser;
	}

	public Long getNumberSupportorUser() {
		return numberSupportorUser;
	}

	public void setNumberSupportorUser(Long numberSupportorUser) {
		this.numberSupportorUser = numberSupportorUser;
	}

	public Long getNumberDevice() {
		return numberDevice;
	}

	public void setNumberDevice(Long numberDevice) {
		this.numberDevice = numberDevice;
	}

	public Long getNumberBlueDevice() {
		return numberBlueDevice;
	}

	public void setNumberBlueDevice(Long numberBlueDevice) {
		this.numberBlueDevice = numberBlueDevice;
	}

	public Long getNumberYellowDevice() {
		return numberYellowDevice;
	}

	public void setNumberYellowDevice(Long numberYellowDevice) {
		this.numberYellowDevice = numberYellowDevice;
	}

	public Long getNumberRedDevice() {
		return numberRedDevice;
	}

	public void setNumberRedDevice(Long numberRedDevice) {
		this.numberRedDevice = numberRedDevice;
	}

	public Long getNumberGrayDevice() {
		return numberGrayDevice;
	}

	public void setNumberGrayDevice(Long numberGrayDevice) {
		this.numberGrayDevice = numberGrayDevice;
	}
    
    
}
