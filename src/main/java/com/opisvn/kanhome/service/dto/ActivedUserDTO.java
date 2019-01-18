package com.opisvn.kanhome.service.dto;


import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the UserDeviceDTO entity.
 */
@JsonInclude(Include.NON_EMPTY)
@Getter
@Setter
public class ActivedUserDTO implements Serializable {

	private static final long serialVersionUID = -7512654272512467275L;

    private String user;
    
    private Date created_date;
}
