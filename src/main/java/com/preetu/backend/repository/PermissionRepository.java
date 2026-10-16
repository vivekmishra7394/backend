package com.preetu.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.preetu.backend.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	
	Optional<Permission> findByCode(String code);
	
	boolean existsByCode(String code);

}
