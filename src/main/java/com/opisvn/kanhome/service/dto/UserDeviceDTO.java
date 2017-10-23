package com.opisvn.kanhome.service.dto;


import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A DTO for the UserDeviceDTO entity.
 */
@JsonInclude(Include.NON_EMPTY)
public class UserDeviceDTO implements Serializable {

	private static final long serialVersionUID = -7512254272512467275L;

	private String id;

    private String name;
    
    private DeviceDTO device;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public DeviceDTO getDevice() {
		return device;
	}

	public void setDevice(DeviceDTO device) {
		this.device = device;
	}
}
