package com.preetu.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.preetu.backend.entity.Users;
import com.preetu.backend.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Users createUser(Users users) {
		return userRepository.save(users);
	}

	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}

	public Users getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));
	}

	public Users updateUserById(Long id, Users updatedUser) {
		Users existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid ID."));

		existingUser.setName(updatedUser.getName());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setPhone(updatedUser.getPhone());

		return userRepository.save(existingUser);

	}

	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}
}
