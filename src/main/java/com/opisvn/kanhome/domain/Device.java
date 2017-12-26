package com.opisvn.kanhome.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.opisvn.kanhome.service.dto.ParamDTO;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * A Device.
 */

@Document(collection = "device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("abbrName")
    private String abbrName;

    @Field("model")
    private String model;

    @Field("firmware")
    private String firmware;

    @Field("passcode")
    private String passcode;

    @Field("countryCode")
    private Long countryCode;

    @Field("ctyCode")
    private Long cityCode;

    @Field("wardCode")
    private Long wardCode;

    @Field("zipCode")
    private Long zipCode;

    @Field("gpsX")
    private Double gpsX;

    @Field("gpsY")
    private Double gpsY;

    @Field("srvName")
    private String srvName;

    @Field("srvPort")
    private Long srvPort;

    @Field("repotIntv")
    private Long repotIntv;

    @Field("saveIntv")
    private Long saveIntv;

    @Field("Keepalive")
    private Long keepalive;

    @Field("distCode")
    private Long distCode;

    @Field("cfgNum")
    private Long cfgNum;
    
    @Field("para")
    private Map<Integer, ParamDTO> para;

    @Field("dsts")
    private String dsts;
    
    public String getDsts() {
        return dsts;
    }

    public Device dsts(String dsts) {
        this.dsts = dsts;
        return this;
    }

    public void setDsts(String dsts) {
        this.dsts = dsts;
    }
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Device name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbrName() {
        return abbrName;
    }

    public Device abbrName(String abbrName) {
        this.abbrName = abbrName;
        return this;
    }

    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
    }

    public String getModel() {
        return model;
    }

    public Device model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFirmware() {
        return firmware;
    }

    public Device firmware(String firmware) {
        this.firmware = firmware;
        return this;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getPasscode() {
        return passcode;
    }

    public Device passcode(String passcode) {
        this.passcode = passcode;
        return this;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public Long getCountryCode() {
        return countryCode;
    }

    public Device countryCode(Long countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(Long countryCode) {
        this.countryCode = countryCode;
    }

    public Long getCityCode() {
        return cityCode;
    }

    public Device cityCode(Long cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public void setCityCode(Long cityCode) {
        this.cityCode = cityCode;
    }

    public Long getWardCode() {
        return wardCode;
    }

    public Device wardCode(Long wardCode) {
        this.wardCode = wardCode;
        return this;
    }

    public void setWardCode(Long wardCode) {
        this.wardCode = wardCode;
    }

    public Long getZipCode() {
        return zipCode;
    }

    public Device zipCode(Long zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(Long zipCode) {
        this.zipCode = zipCode;
    }

    public Double getGpsX() {
        return gpsX;
    }

    public Device gpsX(Double gpsX) {
        this.gpsX = gpsX;
        return this;
    }

    public void setGpsX(Double gpsX) {
        this.gpsX = gpsX;
    }

    public Double getGpsY() {
        return gpsY;
    }

    public Device gpsY(Double gpsY) {
        this.gpsY = gpsY;
        return this;
    }

    public void setGpsY(Double gpsY) {
        this.gpsY = gpsY;
    }

    public String getSrvName() {
        return srvName;
    }

    public Device srvName(String srvName) {
        this.srvName = srvName;
        return this;
    }

    public void setSrvName(String srvName) {
        this.srvName = srvName;
    }

    public Long getSrvPort() {
        return srvPort;
    }

    public Device srvPort(Long srvPort) {
        this.srvPort = srvPort;
        return this;
    }

    public void setSrvPort(Long srvPort) {
        this.srvPort = srvPort;
    }

    public Long getRepotIntv() {
        return repotIntv;
    }

    public Device repotIntv(Long repotIntv) {
        this.repotIntv = repotIntv;
        return this;
    }

    public void setRepotIntv(Long repotIntv) {
        this.repotIntv = repotIntv;
    }

    public Long getSaveIntv() {
        return saveIntv;
    }

    public Device saveIntv(Long saveIntv) {
        this.saveIntv = saveIntv;
        return this;
    }

    public void setSaveIntv(Long saveIntv) {
        this.saveIntv = saveIntv;
    }

    public Long getKeepalive() {
        return keepalive;
    }

    public Device keepalive(Long keepalive) {
        this.keepalive = keepalive;
        return this;
    }

    public void setKeepalive(Long keepalive) {
        this.keepalive = keepalive;
    }

    public Long getDistCode() {
        return distCode;
    }

    public Device distCode(Long distCode) {
        this.distCode = distCode;
        return this;
    }

    public void setDistCode(Long distCode) {
        this.distCode = distCode;
    }

    public Long getCfgNum() {
        return cfgNum;
    }

    public Device cfgNum(Long cfgNum) {
        this.cfgNum = cfgNum;
        return this;
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
        Device device = (Device) o;
        if (device.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), device.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Device{" +
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
}
