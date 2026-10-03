package com.preetu.backend.config;

public class AuthenticatedUser {
	private final String email;
	private final String customerId;
	private final String role;

	public AuthenticatedUser(String email, String role, String customerId) {
		this.email = email;
		this.role = role;
		this.customerId = customerId;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}

	public String getCustomerId() {
		return customerId;
	}
}
