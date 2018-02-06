package com.opisvn.kanhome.service.dto;


import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A DTO for the UserDeviceDTO entity.
 */
@JsonInclude(Include.NON_EMPTY)
public class ActivedUserDTO implements Serializable {

	private static final long serialVersionUID = -7512654272512467275L;

    private String user;
    
    private Date created_date;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
    
}
