package com.opisvn.kanhome.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.opisvn.kanhome.service.dto.ActivedUserDTO;
import com.opisvn.kanhome.service.dto.UserDeviceDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A Topic.
 */

@Document(collection = "appVersion")
public class AppVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("updated_date")
    private Date updatedDate;

    @Field("android_version")
    private Date androidVersion;
    
    @Field("ios_version")
    private Date iosVersion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(Date androidVersion) {
		this.androidVersion = androidVersion;
	}

	public Date getIosVersion() {
		return iosVersion;
	}

	public void setIosVersion(Date iosVersion) {
		this.iosVersion = iosVersion;
	}

}
