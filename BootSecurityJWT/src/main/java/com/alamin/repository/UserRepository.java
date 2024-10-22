package com.alamin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alamin.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(String username);
	Boolean existsByUsername(String username);
}
