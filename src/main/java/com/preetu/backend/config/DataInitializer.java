
package com.preetu.backend.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.preetu.backend.entity.Permission;
import com.preetu.backend.entity.Role;
import com.preetu.backend.repository.PermissionRepository;
import com.preetu.backend.repository.RoleRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeRolesAndPermissions(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {

        return args -> initializeData(
                roleRepository,
                permissionRepository
        );
    }

    @Transactional
    void initializeData(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {

        Map<String, String> permissionDefinitions =
                new LinkedHashMap<>();

        permissionDefinitions.put("PRODUCT_READ", "View products");
        permissionDefinitions.put("PRODUCT_CREATE", "Create products");
        permissionDefinitions.put("PRODUCT_UPDATE", "Update products");
        permissionDefinitions.put("PRODUCT_DELETE", "Delete products");
        permissionDefinitions.put("ORDER_READ", "View orders");
        permissionDefinitions.put("ORDER_CREATE", "Create orders");
        permissionDefinitions.put("USER_READ", "View users");
        permissionDefinitions.put("USER_UPDATE", "Update users");

        Map<String, Permission> permissions = new LinkedHashMap<>();

        permissionDefinitions.forEach((code, description) -> {
            Permission permission = permissionRepository
                    .findByCode(code)
                    .orElseGet(() -> permissionRepository.save(
                            new Permission(code, description)
                    ));

            permissions.put(code, permission);
        });

        Role userRole = findOrCreateRole(
                roleRepository, "USER", "Standard user");

        Role adminRole = findOrCreateRole(
                roleRepository, "ADMIN", "Administrator");

        Role managerRole = findOrCreateRole(
                roleRepository, "MANAGER", "Manager");

        userRole.getPermissions().addAll(Set.of(
                permissions.get("PRODUCT_READ"),
                permissions.get("ORDER_READ"),
                permissions.get("ORDER_CREATE")
        ));

        managerRole.getPermissions().addAll(Set.of(
                permissions.get("PRODUCT_READ"),
                permissions.get("PRODUCT_UPDATE"),
                permissions.get("ORDER_READ")
        ));

        adminRole.getPermissions().addAll(permissions.values());

        roleRepository.save(userRole);
        roleRepository.save(managerRole);
        roleRepository.save(adminRole);
    }

    private Role findOrCreateRole(
            RoleRepository roleRepository,
            String name,
            String description) {

        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(
                        new Role(name, description)
                ));
    }
}
