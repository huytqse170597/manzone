package com.prm.manzone.config;

import com.prm.manzone.entities.User;
import com.prm.manzone.enums.Role;
import com.prm.manzone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppInitConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Bean
    ApplicationRunner init() {
        return args -> {
            if(userRepository.findByEmail("admin@gmail.com").isEmpty()){
                User user = User.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin123"))
                        .firstName("Admin")
                        .lastName("Manzone")
                        .active(true)
                        .avatarUrl("https://i.pinimg.com/736x/cd/4b/d9/cd4bd9b0ea2807611ba3a67c331bff0b.jpg")
                        .phoneNumber("0123456789")
                        .address("Admin House")
                        .role(Role.ADMIN)
                        .build();
                log.info("Default admin user email: {}", user.getEmail());
                log.info("Default admin user password: {}", "admin123");
                userRepository.save(user);
            }
        };
    }
}
