package com.propertyfinder.config;

import com.propertyfinder.entity.User;
import com.propertyfinder.enums.Role;
import com.propertyfinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Only create admin if one doesn't already exist
        if (userRepository.findByEmail("admin@propertyfinder.com").isEmpty()) {

            User admin = User.builder()
                    .fullName("Admin User")
                    .email("admin@propertyfinder.com")
                    .phone("+255757065098")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .role(Role.ADMIN)
                    .active(true)
                    .approved(true)
                    .isVerified(true)
                    .build();

            userRepository.save(admin);
            log.info("✅ Admin user created — email: admin@propertyfinder.com | password: Admin@1234");
        } else {
            log.info("ℹ️  Admin user already exists, skipping creation.");
        }
    }
}