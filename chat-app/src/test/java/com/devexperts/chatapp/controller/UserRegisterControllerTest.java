package com.devexperts.chatapp.controller;

import com.devexperts.chatapp.model.entity.UserEntity;
import com.devexperts.chatapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
class UserRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testOpenRegisterForm() throws Exception {
        mockMvc.
                perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    private static final String TEST_USER_USERNAME = "user";
    private static final String TEST_USER_EMAIL = "user@example.com";
    private static final String TEST_USER_AGE = "25";
    private static final String TEST_USER_COUNTRY = "Bulgaria";
    private static final String TEST_USER_PASSWORD = "12345";

    @Test
    void testRegistrationWithSuccess() throws Exception {
        mockMvc.perform(post("/register").
                        param("username", TEST_USER_USERNAME).
                        param("email", TEST_USER_EMAIL).
                        param("age", TEST_USER_AGE).
                        param("country", TEST_USER_COUNTRY).
                        param("password", TEST_USER_PASSWORD).
                        param("confirmPassword", TEST_USER_PASSWORD).
                        with(csrf()).
                        contentType(MediaType.APPLICATION_FORM_URLENCODED)
                ).
                andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));



        Assertions.assertEquals(1, userRepository.count());

        Optional<UserEntity> newlyCreatedUserOpt = userRepository.findByUsername(TEST_USER_USERNAME);

        Assertions.assertTrue(newlyCreatedUserOpt.isPresent());

        UserEntity newlyCreatedUser = newlyCreatedUserOpt.get();

        Assertions.assertEquals(TEST_USER_EMAIL, newlyCreatedUser.getEmail());
        Assertions.assertEquals(Integer.valueOf(TEST_USER_AGE), newlyCreatedUser.getAge());
        Assertions.assertEquals(TEST_USER_COUNTRY, newlyCreatedUser.getCountry());
        Assertions.assertTrue(passwordEncoder.matches(TEST_USER_PASSWORD, newlyCreatedUser.getPassword()));
    }

    @Test
    void testRegistrationFail() throws Exception {
        mockMvc.perform(post("/register").
                        param("username", TEST_USER_USERNAME).
                        param("email", TEST_USER_EMAIL).
                        param("age", " ").
                        param("country", TEST_USER_COUNTRY).
                        param("password", TEST_USER_PASSWORD).
                        param("confirmPassword", TEST_USER_PASSWORD).
                        with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }



}