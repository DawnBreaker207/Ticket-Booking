package com.dawn.web.config;

import com.dawn.common.constant.Message;
import com.dawn.common.constant.URole;
import com.dawn.common.exception.wrapper.ResourceNotFoundException;
import com.dawn.identity.model.Role;
import com.dawn.identity.model.User;
import com.dawn.identity.repository.RoleRepository;
import com.dawn.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${dawn.app.setup.admin.username}")
    private String adminUsername;

    @Value("${dawn.app.setup.admin.email}")
    private String adminEmail;

    @Value("${dawn.app.setup.admin.password}")
    private String adminPassword;

    @Value("${dawn.app.setup.user.username}")
    private String userUsername;

    @Value("${dawn.app.setup.user.email}")
    private String userEmail;

    @Value("${dawn.app.setup.user.password}")
    private String userPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        try {
            createAdminAccountIfNotExist();
            createUserAccountIfNotExist();
        } catch (Exception e) {
            log.error("Error initializing admin account", e);
        }
    }

    //    Note: This just for demo, don't use this in production
    private void createAdminAccountIfNotExist() {
        if (userRepository.existsByRolesName(URole.ADMIN)) {
            log.info("Admin account already exists");
            return;
        }

        Role role = roleRepository
                .findByName(URole.ADMIN)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Message.Exception.ROLE_NOT_FOUND));

        User user = User
                .builder()
                .username(adminUsername)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .roles(Set.of(role))
                .isDeleted(false)
                .build();

        userRepository.save(user);

        log.info("========================================");
        log.info("   DEMO ADMIN ACCOUNT CREATED");
        log.info("   Username: {}", adminUsername);
        log.info("   Email: {}", adminEmail);
        log.info("   Password: {}", adminPassword);
        log.info("========================================");
    }

    //    Note: This just for demo, don't use this in production
    private void createUserAccountIfNotExist() {
        if (userRepository.existsByRolesName(URole.USER)) {
            log.info("User account already created");
            return;
        }
        Role role = roleRepository
                .findByName(URole.USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Message.Exception.ROLE_NOT_FOUND));

        User user = User
                .builder()
                .username(userUsername)
                .email(userEmail)
                .password(passwordEncoder.encode(userPassword))
                .roles(Set.of(role))
                .isDeleted(false)
                .build();
        userRepository.save(user);

        log.info("========================================");
        log.info("   DEMO USER ACCOUNT CREATED");
        log.info("   Username: {}", userUsername);
        log.info("   Email: {}", userEmail);
        log.info("   Password: {}", userPassword);
        log.info("========================================");
    }
}
