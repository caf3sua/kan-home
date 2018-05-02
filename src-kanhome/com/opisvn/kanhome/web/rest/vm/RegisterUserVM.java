package com.opisvn.kanhome.web.rest.vm;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.opisvn.kanhome.config.Constants;

import lombok.Getter;
import lombok.Setter;

/**
 * View Model extending the UserDTO, which is meant to be used in the user
 * management UI.
 */
@Getter
@Setter
public class RegisterUserVM implements Serializable {

	private static final long serialVersionUID = 872877794432352578L;
	
	private String fullname;
	
	@Email
	@Size(min = 5, max = 100)
	private String email;
	
	private String username;
	
	private String password;
	
	@NotBlank
	@Pattern(regexp = Constants.PHONENUMBER_REGEX)
	@Size(min = 1, max = 15)
	private String phonenumber;
	
	private String address;
	
	@Size(min = 2, max = 5)
	private String langKey;
}
