package com.opisvn.kanhome.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DeviceStat entity.
 */
public class DeviceStatDTO implements Serializable {

    private String id;

    private String type;

    private String clientid;

    private String time;

    private String u;

    private String wQ;
    
    private String dsts;

    public String getDsts() {
		return dsts;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getwQ() {
        return wQ;
    }

    public void setwQ(String wQ) {
        this.wQ = wQ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceStatDTO deviceStatDTO = (DeviceStatDTO) o;
        if(deviceStatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deviceStatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeviceStatDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", clientid='" + getClientid() + "'" +
            ", time='" + getTime() + "'" +
            ", u='" + getU() + "'" +
            ", wQ='" + getwQ() + "'" +
            "}";
    }
}
