package com.opisvn.kanhome.web.rest.vm;

import com.opisvn.kanhome.domain.AppVersion;

/**
 * View Model object for storing a user's credentials.
 */
public class AppVersionVM {

    private String message;

    private int code;
    
    private AppVersion data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public AppVersion getData() {
		return data;
	}

	public void setData(AppVersion data) {
		this.data = data;
	}
    
}
