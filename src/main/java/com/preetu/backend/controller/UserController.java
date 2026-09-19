package com.preetu.backend.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.preetu.backend.dto.UserRequest;
import com.preetu.backend.dto.UserResponse;
import com.preetu.backend.entity.Users;
import com.preetu.backend.service.UserService;

import jakarta.validation.Valid;

@Controller
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/create-user")
	public UserResponse createUser(@Valid @RequestBody UserRequest request) {
		System.out.println(request.getName());

		System.out.println(request.getEmail());

		System.out.println(request.getPhone());
		return userService.createUser(request);
	}

	@GetMapping
	public List<UserResponse> getAllUsers() {
		return userService.getAllUsers();

	}

	@GetMapping("/get-user-by-id/{id}")
	public UserResponse getUserById(@Valid @PathVariable Long id) {
		return userService.getUserById(id);
	}

	@PutMapping("/update-user/{id}")
	public UserResponse updateUserById(@Valid @PathVariable Long id, @RequestBody UserRequest updatedUser) {
		return userService.updateUserById(id, updatedUser);
	}

	@DeleteMapping("/delete-user/{id}")
	public String deleteUserById(@Valid @PathVariable Long id) {
		userService.deleteUserById(id);
		return "User deleted successfully.";
	}

}
