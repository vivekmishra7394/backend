package com.preetu.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.preetu.backend.dto.UserRequest;
import com.preetu.backend.dto.UserResponse;
import com.preetu.backend.entity.Users;
import com.preetu.backend.exception.ConflictException;
import com.preetu.backend.exception.ResourceNotFoundException;
import com.preetu.backend.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// CREATE USER
	public UserResponse createUser(UserRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ConflictException("User with email already exists+ " + request.getEmail());
		}

		Users users = new Users();

		users.setName(request.getName());
		users.setEmail(request.getEmail());
		users.setPhone(request.getPhone());

		Users savedUsers = userRepository.save(users);

		return toResponse(savedUsers);
	}

	// GET ALL USERS
	public Page<UserResponse> getAllUsers(Pageable pageable) {

		return userRepository.findAll(pageable).map(this::toResponse);

	}

	// GET USER BY ID
	public UserResponse getUserById(Long id) {

		Users users = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		return toResponse(users);
	}

	// UPDATE USER
	public UserResponse updateUserById(Long id, UserRequest updatedUser) {

		Users existingUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		existingUser.setName(updatedUser.getName());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setPhone(updatedUser.getPhone());

		Users updatedUsers = userRepository.save(existingUser);

		return toResponse(updatedUsers);
	}

	// DELETE USER BY ID
	public void deleteUserById(Long id) {

		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found with id: " + id);
		}

		userRepository.deleteById(id);
	}

	// ENTITY → RESPONSE DTO
	private UserResponse toResponse(Users user) {

		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone());
	}
}