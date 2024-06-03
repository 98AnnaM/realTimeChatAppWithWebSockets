package com.devexperts.chatapp.init;

import com.devexperts.chatapp.model.entity.RoleEntity;
import com.devexperts.chatapp.model.entity.UserEntity;
import com.devexperts.chatapp.model.enums.RoleEnum;
import com.devexperts.chatapp.repository.RoleRepository;
import com.devexperts.chatapp.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class Init implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Init(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Initialization logic here
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAfterStartup() {
        if (roleRepository.count() == 0) {
            RoleEntity roleUser = new RoleEntity(RoleEnum.USER);
            RoleEntity roleAdmin = new RoleEntity(RoleEnum.ADMIN);
            roleRepository.save(roleUser);
            roleRepository.save(roleAdmin);
        }

        if (userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setUsername("amileva");
            admin.setEmail("amileva@gmail.com");
            admin.setAge(25);
            admin.setCountry("Bulgaria");
            admin.setDate(new Date());
            admin.setPassword(passwordEncoder.encode("12345"));

            RoleEntity roleUser = roleRepository.findByRole(RoleEnum.USER).orElse(null);
            RoleEntity roleAdmin = roleRepository.findByRole(RoleEnum.ADMIN).orElse(null);

            admin.setRoles(List.of(roleUser, roleAdmin));
            userRepository.save(admin);

        }
    }
}