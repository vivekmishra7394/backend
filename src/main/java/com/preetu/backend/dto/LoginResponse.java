package com.preetu.backend.dto;

public class LoginResponse {

	private UserResponse user;
	private String token;

	public UserResponse getUser() {
		return user;
	}

	public String getToken() {
		return token;
	}

	public LoginResponse(UserResponse user, String token) {
		this.user = user;
		this.token = token;
	}
}
