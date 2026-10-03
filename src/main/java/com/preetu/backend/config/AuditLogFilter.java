package com.preetu.backend.config;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.preetu.backend.service.AuditLogService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuditLogFilter extends OncePerRequestFilter {

    private final AuditLogService auditLogService;

    public AuditLogFilter(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        // Only API requests should be audited
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();

        String action = request.getRequestURI();
        String method = request.getMethod();
        String ipAddress = request.getRemoteAddr();

        String userAgent = request.getHeader("User-Agent");
        String browser = getBrowserName(userAgent);

        try {

            // Execute the actual API
            filterChain.doFilter(request, response);

        } finally {

            int status = response.getStatus();

            String customerId = null;

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null
                    && authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser) {

                customerId = authenticatedUser.getCustomerId();
            }

            auditLogService.saveAuditLog(
                    customerId,
                    requestId,
                    action,
                    method,
                    null,
                    null,
                    String.valueOf(status),
                    ipAddress,
                    browser,
                    status >= 400 ? "API request failed" : null
            );
        }
    }

    private String getBrowserName(String userAgent) {

        if (userAgent == null || userAgent.isBlank()) {
            return "Unknown";
        }

        // Microsoft Edge
        if (userAgent.contains("Edg/")) {
            return "Edge";
        }

        // Opera
        if (userAgent.contains("OPR/")) {
            return "Opera";
        }

        // Brave
        if (userAgent.contains("Brave")) {
            return "Brave";
        }

        // Chrome
        if (userAgent.contains("Chrome/")) {
            return "Chrome";
        }

        // Firefox
        if (userAgent.contains("Firefox/")) {
            return "Firefox";
        }

        // Safari
        if (userAgent.contains("Safari/")) {
            return "Safari";
        }

        // Unknown browser/client
        return userAgent;
    }
}