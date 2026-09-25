package com.preetu.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.preetu.backend.config.JwtService;
import com.preetu.backend.dto.LoginRequest;
import com.preetu.backend.dto.LoginResponse;
import com.preetu.backend.dto.UserRequest;
import com.preetu.backend.dto.UserResponse;
import com.preetu.backend.entity.Users;
import com.preetu.backend.exception.ConflictException;
import com.preetu.backend.exception.ResourceNotFoundException;
import com.preetu.backend.repository.UserRepository;

@Service
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final JwtService jwtService;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	// CREATE USER
	public UserResponse createUser(UserRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ConflictException("User with email already exists: " + request.getEmail());
		}

		Users users = new Users();

		users.setName(request.getName());
		users.setEmail(request.getEmail());
		users.setPhone(request.getPhone());
		users.setPassword(passwordEncoder.encode(request.getPassword()));

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
	public UserResponse toResponse(Users user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone());
	}

	public String generateToken(LoginRequest request) {
		Users users = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Email or password"));

		if (!passwordEncoder.matches(request.getPassword(), users.getPassword())) {
			throw new RuntimeException("Invalid Email or password");
		}

		return jwtService.generateToken(users.getId(), users.getEmail());
	}

	public LoginResponse login(LoginRequest request) {
		Users users = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Email or password"));

		if (!passwordEncoder.matches(request.getPassword(), users.getPassword())) {
			throw new RuntimeException("Invalid Email or password");
		}
		String token = jwtService.generateToken(users.getId(), users.getEmail());

		return new LoginResponse(toResponse(users), token);
	}
}