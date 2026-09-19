package com.preetu.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.preetu.backend.dto.UserRequest;
import com.preetu.backend.dto.UserResponse;
import com.preetu.backend.entity.Users;
import com.preetu.backend.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserResponse createUser(UserRequest request) {
		Users users = new Users();
		users.setName(request.getName());
		users.setEmail(request.getEmail());
		users.setPhone(request.getPhone());

		Users savedUsers = userRepository.save(users);

		return toResponse(savedUsers);

	}

	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(this::toResponse).toList();
	}

	public UserResponse getUserById(Long id) {

		Users users = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));
		return toResponse(users);
	}

	public UserResponse updateUserById(Long id, UserRequest updatedUser) {
		Users existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid ID."));

		existingUser.setName(updatedUser.getName());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setPhone(updatedUser.getPhone());
		
		Users updatedUsers = userRepository.save(existingUser);

		return toResponse(updatedUsers);

	}

	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

	private UserResponse toResponse(Users users) {
		return new UserResponse(users.getId(), users.getName(), users.getEmail(), users.getPhone());
	}
}
