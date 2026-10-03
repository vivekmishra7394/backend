package com.preetu.backend.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        // Read JWT from cookie
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {

                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // If token exists, validate it
        if (token != null) {
        	  System.out.println("JWT cookie received");

            try {

            	
                String email = jwtService.extractEmail(token);
                String role = jwtService.extractRole(token);
                String customerId = jwtService.extractCustomerId(token);
             

                if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                	  SimpleGrantedAuthority authority =
                              new SimpleGrantedAuthority("ROLE_" + role);

                      UsernamePasswordAuthenticationToken authentication =
                              new UsernamePasswordAuthenticationToken(
                            		  
                                      new AuthenticatedUser(email, role,customerId),
                                      null, 
                                      List.of(authority)
                              );            	
                

                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(authentication);
                }
                
    
            } catch (Exception e) {

                // Invalid/expired JWT
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}