package com.preetu.backend.repository;

import com.preetu.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}