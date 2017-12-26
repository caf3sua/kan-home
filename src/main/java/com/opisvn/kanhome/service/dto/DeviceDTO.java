package com.opisvn.kanhome.service.dto;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A DTO for the Device entity.
 */
@JsonInclude(Include.NON_EMPTY)
public class DeviceDTO implements Serializable {

    private String id;

    private String name;

    private String abbrName;

    private String model;

    private String firmware;

    private String passcode;

    private Long countryCode;

    private Long cityCode;

    private Long wardCode;

    private Long zipCode;

    private Double gpsX;

    private Double gpsY;

    private String srvName;

    private Long srvPort;

    private Long repotIntv;

    private Long saveIntv;

    private Long keepalive;

    private Long distCode;

    private Long cfgNum;
    
    private String dsts;
    
    private Map<Integer, ParamDTO> para;
    
    private List<UserDTO> users;
    
    // More attribute
    private String status;
    
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

    public String getAbbrName() {
        return abbrName;
    }

    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public Long getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Long countryCode) {
        this.countryCode = countryCode;
    }

    public Long getCityCode() {
        return cityCode;
    }

    public void setCityCode(Long cityCode) {
        this.cityCode = cityCode;
    }

    public Long getWardCode() {
        return wardCode;
    }

    public void setWardCode(Long wardCode) {
        this.wardCode = wardCode;
    }

    public Long getZipCode() {
        return zipCode;
    }

    public void setZipCode(Long zipCode) {
        this.zipCode = zipCode;
    }

    public Double getGpsX() {
        return gpsX;
    }

    public void setGpsX(Double gpsX) {
        this.gpsX = gpsX;
    }

    public Double getGpsY() {
        return gpsY;
    }

    public void setGpsY(Double gpsY) {
        this.gpsY = gpsY;
    }

    public String getSrvName() {
        return srvName;
    }

    public void setSrvName(String srvName) {
        this.srvName = srvName;
    }

    public Long getSrvPort() {
        return srvPort;
    }

    public void setSrvPort(Long srvPort) {
        this.srvPort = srvPort;
    }

    public Long getRepotIntv() {
        return repotIntv;
    }

    public void setRepotIntv(Long repotIntv) {
        this.repotIntv = repotIntv;
    }

    public Long getSaveIntv() {
        return saveIntv;
    }

    public void setSaveIntv(Long saveIntv) {
        this.saveIntv = saveIntv;
    }

    public Long getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(Long keepalive) {
        this.keepalive = keepalive;
    }

    public Long getDistCode() {
        return distCode;
    }

    public void setDistCode(Long distCode) {
        this.distCode = distCode;
    }

    public Long getCfgNum() {
        return cfgNum;
    }

    public void setCfgNum(Long cfgNum) {
        this.cfgNum = cfgNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if(deviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", abbrName='" + getAbbrName() + "'" +
            ", model='" + getModel() + "'" +
            ", firmware='" + getFirmware() + "'" +
            ", passcode='" + getPasscode() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", cityCode='" + getCityCode() + "'" +
            ", wardCode='" + getWardCode() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", gpsX='" + getGpsX() + "'" +
            ", gpsY='" + getGpsY() + "'" +
            ", srvName='" + getSrvName() + "'" +
            ", srvPort='" + getSrvPort() + "'" +
            ", repotIntv='" + getRepotIntv() + "'" +
            ", saveIntv='" + getSaveIntv() + "'" +
            ", keepalive='" + getKeepalive() + "'" +
            ", distCode='" + getDistCode() + "'" +
            ", cfgNum='" + getCfgNum() + "'" +
            "}";
    }

	public Map<Integer, ParamDTO> getPara() {
		return para;
	}

	public void setPara(Map<Integer, ParamDTO> para) {
		this.para = para;
	}

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDsts() {
		return dsts;
	}

	public void setDsts(String dsts) {
		this.dsts = dsts;
	}


}
