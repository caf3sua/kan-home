package com.opisvn.kanhome.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.opisvn.kanhome.service.dto.ActivedUserDTO;
import com.opisvn.kanhome.service.dto.UserDeviceDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Topic.
 */

@Document(collection = "activeCodes")
public class ActiveCodes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("activeCode")
    private String activeCode;

    @Field("remain")
    private Integer remain;
    
    @Field("activedUsers")
    private List<ActivedUserDTO> activedUsers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public Integer getRemain() {
		return remain;
	}

	public void setRemain(Integer remain) {
		this.remain = remain;
	}

	public List<ActivedUserDTO> getActivedUsers() {
		if (activedUsers == null) {
			return new ArrayList<ActivedUserDTO>();
		}
		return activedUsers;
	}

	public void setActivedUsers(List<ActivedUserDTO> activedUsers) {
		this.activedUsers = activedUsers;
	}
    
    
}
