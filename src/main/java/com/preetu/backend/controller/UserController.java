package com.preetu.backend.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.preetu.backend.entity.Users;
import com.preetu.backend.service.UserService;


@Controller
@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/create-user")
	public Users createUser(@RequestBody Users users) {
		System.out.println(users.getName());

		System.out.println(users.getEmail());

		System.out.println(users.getPhone());
		return userService.createUser(users);
	}

	@GetMapping
	public List<Users> getAllUsers() {
		return userService.getAllUsers();

	}
	
	@GetMapping("/get-user-by-id/{id}")
	public Users getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	@PutMapping("/update-user/{id}")
	public Users updateUserById(@PathVariable Long id, @RequestBody Users users) {
		return userService.updateUserById(id, users);
	}

	@DeleteMapping("/delete-user/{id}")
	public String deleteUserById(@PathVariable Long id) {
		userService.deleteUserById(id);
		return "User deleted successfully.";
	}
	
	

}
