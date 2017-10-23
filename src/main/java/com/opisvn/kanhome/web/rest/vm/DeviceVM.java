package com.opisvn.kanhome.web.rest.vm;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.opisvn.kanhome.service.dto.DeviceDTO;

public class DeviceVM implements Serializable {

	private static final long serialVersionUID = 307323648921329841L;

	private DeviceDTO search;
	
	private int page;
    private int size;
    private List<String> sort;

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<String> getSort() {
		return sort;
	}
	public void setSort(List<String> sort) {
		this.sort = sort;
	}
	public DeviceDTO getSearch() {
		return search;
	}
	public void setSearch(DeviceDTO search) {
		this.search = search;
	}
}
