package com.preetu.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
	private final JwtAuthFilter jwtAuthFilter;
	private final AuditLogFilter auditLogFilter;

	SecurityConfig(JwtAuthFilter jwtAuthFilter,AuditLogFilter auditLogFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.auditLogFilter = auditLogFilter;	
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)

				.authorizeHttpRequests(auth -> auth

						// Swagger ko public rakhenge
						.requestMatchers("/swagger-ui/**", "/swagger-ui.html"	, "/v3/api-docs" ,"/v3/api-docs/**",
								"/api/users/user-login", "/api/users/create-user", "/api/users/user-logout").permitAll()
					
						.requestMatchers("/api/users/**").hasRole("USER")
					    .requestMatchers("/api/products/**").hasRole("ADMIN")
						.anyRequest().authenticated())

				.exceptionHandling(exception -> exception.authenticationEntryPoint((request, response,
						authException) -> response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")))

				  .addFilterBefore(
			                jwtAuthFilter,
			                UsernamePasswordAuthenticationFilter.class
			            )

			            // Audit after JWT authentication
			            .addFilterAfter(
			                auditLogFilter,
			                JwtAuthFilter.class
			            );

		return http.build();
	}
}