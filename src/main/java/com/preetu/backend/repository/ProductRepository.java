package com.preetu.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.preetu.backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByName(String name);
	
	
	 boolean existsByName(String name);

	    @Modifying
	    @Query("DELETE FROM Product p WHERE p.name = :name")
	    void deleteByName(@Param("name") String name);
}
