package com.devexperts.chatapp.user_details;

import com.devexperts.chatapp.model.entity.RoleEntity;
import com.devexperts.chatapp.model.entity.UserEntity;
import com.devexperts.chatapp.model.enums.RoleEnum;
import com.devexperts.chatapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsTest {

    private UserEntity testUser;
    private RoleEntity adminRole, userRole;

    private CustomUserDetailsService serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void init() {

        //ARRANGE
        serviceToTest = new CustomUserDetailsService(mockUserRepository);

        adminRole = new RoleEntity();
        adminRole.setRole(RoleEnum.ADMIN);

        userRole = new RoleEntity();
        userRole.setRole(RoleEnum.USER);

        testUser = new UserEntity();
        testUser.setUsername("anna");
        testUser.setPassword("12345");
        testUser.setRoles(List.of(adminRole, userRole));
    }

    @Test
    void testUserNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("invalid_username")
        );
    }

    @Test
    void testUserFound() {

        // Arrange
        Mockito.when(mockUserRepository.findByUsername(testUser.getUsername())).
                thenReturn(Optional.of(testUser));

        // Act
        CustomUserDetails userDetails = (CustomUserDetails) serviceToTest.loadUserByUsername(testUser.getUsername());

        // Assert

        Assertions.assertEquals(userDetails.getUsername(), testUser.getUsername());
        Assertions.assertEquals(userDetails.getId(), testUser.getId());
        Assertions.assertEquals(userDetails.getPassword(), testUser.getPassword());

        Assertions.assertEquals(2, userDetails.getAuthorities().size());

        String expectedRoles = "ROLE_ADMIN, ROLE_USER";
        String actualRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
                Collectors.joining(", "));

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

}