package com.devexperts.chatapp.util;

import com.devexperts.chatapp.model.entity.RoleEntity;
import com.devexperts.chatapp.model.entity.UserEntity;
import com.devexperts.chatapp.model.enums.RoleEnum;
import com.devexperts.chatapp.repository.RoleRepository;
import com.devexperts.chatapp.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestDataUtils {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public TestDataUtils(UserRepository userRepository,
                         RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            RoleEntity adminRole = new RoleEntity().setRole(RoleEnum.ADMIN);
            RoleEntity userRole = new RoleEntity().setRole(RoleEnum.USER);

            roleRepository.save(adminRole);
            roleRepository.save(userRole);
        }
    }

    public UserEntity createTestAdmin(String username) {

        initRoles();

        UserEntity admin = new UserEntity().
                setUsername(username).
                setEmail("admin@example.com").
                setAge(21).
                setCountry("Bulgaria").
                setDate(new Date()).
                setRoles(roleRepository.findAll())
                .setPassword("12345");

        return userRepository.save(admin);
    }

    public UserEntity createTestUser(String username) {

        initRoles();

        UserEntity user = new UserEntity().
                setUsername(username).
                setEmail("user@example.com").
                setAge(23).
                setCountry("Bulgaria").
                setDate(new Date()).
                setRoles(roleRepository.
                        findAll().stream().
                        filter(r -> r.getRole() != RoleEnum.ADMIN).
                        toList())
                .setPassword("12345");

        return userRepository.save(user);
    }

    public void cleanUpDatabase() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
