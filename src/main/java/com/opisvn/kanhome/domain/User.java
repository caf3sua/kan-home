package com.opisvn.kanhome.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opisvn.kanhome.config.Constants;
import com.opisvn.kanhome.service.dto.UserDeviceDTO;

/**
 * A user.
 */
@Document(collection = "user")
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 100)
    @Indexed
    private String username;

    @JsonIgnore
    @NotNull
    @Size(min = 1, max = 60)
    private String password;

    @Size(max = 50)
    @Field("first_name")
    private String firstName;

    @Size(max = 50)
    @Field("last_name")
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    @Indexed
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    @Field("lang_key")
    private String langKey;

    @Size(max = 256)
    @Field("image_url")
    private String imageUrl;

    @Size(max = 20)
    @Field("activation_key")
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Field("activation_code")
    @JsonIgnore
    private String activationCode;
    
    @Size(max = 20)
    @Field("reset_key")
    @JsonIgnore
    private String resetKey;


    @Field("reset_date")
    private Instant resetDate = null;

    @JsonIgnore
    private Set<Authority> authorities = new HashSet<>();
    
    @Size(max = 256)
    @Field("fullname")
    private String fullname;
    
    @Field("status")
	private Integer status;
    
    @Size(max = 15)
    @Field("phonenumber")
    private String phonenumber;
    
    @Size(max = 256)
    @Field("address")
    private String address;
    
    @Field("gender")
    private Integer gender;
    
    @Field("birthday")
    private Date birthday;
    
    @Field("latest_notification_id")
    private Long latestNotificationId;

    @Field("userDevices")
    private List<UserDeviceDTO> userDevices;
    
    public List<UserDeviceDTO> getUserDevices() {
		return userDevices;
	}

	public void setUserDevices(List<UserDeviceDTO> userDevices) {
		this.userDevices = userDevices;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    //Lowercase the username before saving it in database
    public void setUsername(String username) {
        this.username = username.toLowerCase(Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }
    
    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
       return resetDate;
    }

    public void setResetDate(Instant resetDate) {
       this.resetDate = resetDate;
    }
    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getFullname() {
		return fullname;
	}

	public Integer getStatus() {
		return status;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public String getAddress() {
		return address;
	}

	public Integer getGender() {
		return gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public Long getLatestNotificationId() {
		return latestNotificationId;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setLatestNotificationId(Long latestNotificationId) {
		this.latestNotificationId = latestNotificationId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
