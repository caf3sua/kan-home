package com.opisvn.kanhome.web.rest.vm;

/**
 * View Model object for storing a user's credentials.
 */
public class PasswordVM {

    private String username;

    private String password;
    
    private String oldPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

}
