package com.opisvn.kanhome.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DeviceStat.
 */

@Document(collection = "deviceStat")
public class DeviceStat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("type")
    private String type;

    @Field("clientid")
    private String clientid;

    @Field("time")
    private String time;

    @Field("u")
    private String u;

    @Field("WQ")
    private String wQ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public DeviceStat type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClientid() {
        return clientid;
    }

    public DeviceStat clientid(String clientid) {
        this.clientid = clientid;
        return this;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getTime() {
        return time;
    }

    public DeviceStat time(String time) {
        this.time = time;
        return this;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getU() {
        return u;
    }

    public DeviceStat u(String u) {
        this.u = u;
        return this;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getwQ() {
        return wQ;
    }

    public DeviceStat wQ(String wQ) {
        this.wQ = wQ;
        return this;
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
        DeviceStat deviceStat = (DeviceStat) o;
        if (deviceStat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deviceStat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeviceStat{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", clientid='" + getClientid() + "'" +
            ", time='" + getTime() + "'" +
            ", u='" + getU() + "'" +
            ", wQ='" + getwQ() + "'" +
            "}";
    }
}
