package com.opisvn.kanhome.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.opisvn.kanhome.service.dto.FilterDTO;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * A ModelDevice.
 */

@Document(collection = "models")
public class ModelDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("model")
    private String model;

    @Field("waranty")
    private Long waranty;

    @Field("imageUrl")
    private String imageUrl;
    
    @Field("filters")
    private Map<Integer, FilterDTO> filters;

    public Map<Integer, FilterDTO> getFilters() {
		return filters;
	}

	public void setFilters(Map<Integer, FilterDTO> filters) {
		this.filters = filters;
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

    public ModelDevice name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public ModelDevice model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getWaranty() {
        return waranty;
    }

    public ModelDevice waranty(Long waranty) {
        this.waranty = waranty;
        return this;
    }

    public void setWaranty(Long waranty) {
        this.waranty = waranty;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ModelDevice imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModelDevice modelDevice = (ModelDevice) o;
        if (modelDevice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), modelDevice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ModelDevice{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", model='" + getModel() + "'" +
            ", waranty='" + getWaranty() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            "}";
    }
}
