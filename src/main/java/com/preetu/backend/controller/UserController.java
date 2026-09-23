package com.preetu.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.preetu.backend.dto.ApiResponse;
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
	public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
		System.out.println(request.getName());

		System.out.println(request.getEmail());

		System.out.println(request.getPhone());
		UserResponse users = userService.createUser(request);

		return new ApiResponse<>(true, "User created successfully", users);
	}

	@GetMapping
	public ApiResponse<Page<UserResponse>> getAllUsers(Pageable pageable) {
		Page<UserResponse> users = userService.getAllUsers(pageable);
		return new ApiResponse<>(true, "Fetched All users", users);

	}

	@GetMapping("/get-user-by-id/{id}")
	public ApiResponse<UserResponse> getUserById(@Valid @PathVariable Long id) {
		UserResponse users = userService.getUserById(id);

		return new ApiResponse<>(true, "User fetched successfully", users);
	}

	@PutMapping("/update-user/{id}")
	public ApiResponse<UserResponse> updateUserById(@Valid @PathVariable Long id,
			@RequestBody UserRequest updatedUser) {
		UserResponse users = userService.updateUserById(id, updatedUser);
		return new ApiResponse<>(true, "User updated successfully", users);
	}

	@DeleteMapping("/delete-user/{id}")
	public ApiResponse<String> deleteUserById(@Valid @PathVariable Long id) {
		userService.deleteUserById(id);
		return new ApiResponse<>(true, "User deleted successfully.", null);
	}

}
