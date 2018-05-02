package com.opisvn.kanhome.service.dto;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the ModelDevice entity.
 */
public class ModelDeviceDTO implements Serializable {

    private String id;

    private String name;

    private String model;

    private Long waranty;

    private String imageUrl;
    
    private Map<Integer, FilterDTO> filters;
    
    private List<FilterDTO> lstFilters;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getWaranty() {
        return waranty;
    }

    public void setWaranty(Long waranty) {
        this.waranty = waranty;
    }

    public String getImageUrl() {
        return imageUrl;
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

        ModelDeviceDTO modelDeviceDTO = (ModelDeviceDTO) o;
        if(modelDeviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), modelDeviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ModelDeviceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", model='" + getModel() + "'" +
            ", waranty='" + getWaranty() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            "}";
    }

	public Map<Integer, FilterDTO> getFilters() {
		return filters;
	}

	public void setFilters(Map<Integer, FilterDTO> filters) {
		this.filters = filters;
	}

	public List<FilterDTO> getLstFilters() {
		return lstFilters;
	}

	public void setLstFilters(List<FilterDTO> lstFilters) {
		this.lstFilters = lstFilters;
	}
}
