package com.opisvn.kanhome.service.dto;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.opisvn.kanhome.config.Constants;
import com.opisvn.kanhome.domain.Authority;
import com.opisvn.kanhome.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
@JsonInclude(Include.NON_EMPTY)
public class UserDTO {

    private String id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 100)
    private String username;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;
    
    private String fullname;
	private Integer status;
    private String phonenumber;
    private String address;
    private Integer gender;
    private Date birthday;
    private Long latestNotificationId;
    
    private String password;
    
    private List<UserDeviceDTO> userDevices;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(), user.getImageUrl(), user.getLangKey(),
            user.getCreatedBy(), user.getCreatedDate(), user.getLastModifiedBy(), user.getLastModifiedDate(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()),
            user.getFullname(), user.getStatus(), user.getPhonenumber(),
            user.getAddress(), user.getGender(), user.getBirthday(), user.getLatestNotificationId(), user.getUserDevices());
    }

    public UserDTO(String id, String login, String firstName, String lastName,
            String email, boolean activated, String imageUrl, String langKey,
            String createdBy, Instant createdDate, String lastModifiedBy, Instant lastModifiedDate,
            Set<String> authorities, String fullname, Integer status, String phonenumber,
            String address, Integer gender, Date birthday, Long latestNotificationId, List<UserDeviceDTO> userDevices) {

            this.id = id;
            this.username = login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.activated = activated;
            this.imageUrl = imageUrl;
            this.langKey = langKey;
            this.createdBy = createdBy;
            this.createdDate = createdDate;
            this.lastModifiedBy = lastModifiedBy;
            this.lastModifiedDate = lastModifiedDate;
            this.authorities = authorities;
            this.fullname = fullname;
            this.status = status;
            this.phonenumber = phonenumber;
            this.address = address;
            this.gender = gender;
            this.birthday = birthday;
            this.latestNotificationId = latestNotificationId;
            this.userDevices = userDevices;
        }
    
    public UserDTO(String id, String login, String firstName, String lastName,
        String email, boolean activated, String imageUrl, String langKey,
        String createdBy, Instant createdDate, String lastModifiedBy, Instant lastModifiedDate,
        Set<String> authorities) {

        this.id = id;
        this.username = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.imageUrl = imageUrl;
        this.langKey = langKey;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.authorities = authorities;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + username + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            "}";
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

	public List<UserDeviceDTO> getUserDevices() {
		return userDevices;
	}

	public void setUserDevices(List<UserDeviceDTO> userDevices) {
		this.userDevices = userDevices;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
