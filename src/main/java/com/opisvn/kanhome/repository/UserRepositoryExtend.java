package com.opisvn.kanhome.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.opisvn.kanhome.domain.User;
import com.opisvn.kanhome.service.dto.UserDTO;


@Repository
public interface UserRepositoryExtend {

	List<User> findByDeviceId(String deviceId);
	
	Page<User> search(Pageable pageable, UserDTO dto);
	
	Long countByRole(String role);
}
