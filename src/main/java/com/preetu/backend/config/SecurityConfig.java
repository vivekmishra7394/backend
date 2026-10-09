package com.preetu.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final AuditLogFilter auditLogFilter;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuditLogFilter auditLogFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.auditLogFilter = auditLogFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)

				.authorizeHttpRequests(auth -> auth

						// =========================
						// PUBLIC
						// =========================

						.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs", "/v3/api-docs/**")
						.permitAll()

						.requestMatchers("/api/users/create-user", "/api/users/user-login", "/api/users/user-logout")
						.permitAll()

						// =========================
						// USER + ADMIN
						// =========================

						.requestMatchers("/api/users/me").hasAnyRole("USER", "ADMIN")

						.requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**")
						.hasAnyRole("USER", "ADMIN")

						// =========================
						// ADMIN ONLY
						// =========================

						.requestMatchers(HttpMethod.POST, "/api/products", "/api/products/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")

						.requestMatchers(HttpMethod.GET, "/api/users/get-user-by-id/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.PUT, "/api/users/update-user/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.DELETE, "/api/users/delete-user/**").hasRole("ADMIN")

						.requestMatchers("/api/users/admin-login").hasRole("ADMIN")

						// =========================
						// EVERYTHING ELSE
						// =========================

						.anyRequest().authenticated())

				// =========================
				// 401 / 403
				// =========================

				.exceptionHandling(exception -> exception

						.authenticationEntryPoint((request, response, authException) -> {
							response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
						})

						.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied");
						}))

				// =========================
				// FILTER ORDER
				// =========================

				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

				.addFilterAfter(auditLogFilter, JwtAuthFilter.class);

		return http.build();
	}
}