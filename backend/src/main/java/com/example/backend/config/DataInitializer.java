package com.example.backend.config;

import com.example.backend.constant.Message;
import com.example.backend.constant.URole;
import com.example.backend.exception.wrapper.RoleNotFoundException;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
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
    private String username;

    @Value("${dawn.app.setup.admin.email}")
    private String email;

    @Value("${dawn.app.setup.admin.password}")
    private String password;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        try{
            createAdminAccountIfNotExist();
        }catch (Exception e){
            log.error("Error initializing admin account", e);
        }
    }

    //    Note: This just for demo, don't use this in production
    private void createAdminAccountIfNotExist() {
        if (userRepository.existsByRolesName(URole.ADMIN)) {
            log.info("Admin Account already exists");
            return;
        }

        Role role = roleRepository
                .findByName(URole.ADMIN)
                .orElseThrow(() -> new RoleNotFoundException(Message.Exception.ROLE_NOT_FOUND));

        User user = User
                .builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(role))
                .isDeleted(false)
                .build();

        userRepository.save(user);

        log.info("========================================");
        log.info("✅ DEMO ADMIN ACCOUNT CREATED");
        log.info("   Username: {}", user);
        log.info("   Email: {}", email);
        log.info("   Password: {}", password);
        log.info("========================================");
    }
}
