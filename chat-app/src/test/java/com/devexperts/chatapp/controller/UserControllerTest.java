package com.devexperts.chatapp.controller;

import com.devexperts.chatapp.model.entity.UserEntity;
import com.devexperts.chatapp.util.TestDataUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private UserEntity testUser;
    private UserEntity testAdmin;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("user");
        testAdmin = testDataUtils.createTestAdmin("admin");
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testOpenEditProfilePageByOwnerUser_Success() throws Exception {
        mockMvc.perform(get("/edit/{id}", testUser.getId()).
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("edit"));
    }

    @Test
    void testViewProfileByAnonymousUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/edit/{id}", testUser.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testUpdateUserInformationByOwner_Success() throws Exception {

        MvcResult result = mockMvc.perform(
                        put("/edit/{id}", testUser.getId())
                                .param("username", "NewName")
                                .param("age", "40")
                                .param("email", "newemail@user.com")
                                .param("country", "NewCountry")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/report")) // Add this line
                .andReturn();

        UserEntity updatedUser = testDataUtils.getUserRepository().findById(testUser.getId()).orElse(null);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("NewName", updatedUser.getUsername());
        Assertions.assertEquals("NewCountry", updatedUser.getCountry());
        Assertions.assertEquals("newemail@user.com", updatedUser.getEmail());
        Assertions.assertEquals(40, updatedUser.getAge());
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testViewRecordsByAuthenticatedUser_Success() throws Exception {
        mockMvc.perform(get("/report").
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("report"));
    }

    @Test
    void testViewRecordsByAnonymous_Forbiden() throws Exception {
        mockMvc.perform(get("/report").
                        with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andReturn();
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testViewUsersUnder18ByAuthenticatedUser_Success() throws Exception {
        mockMvc.perform(get("/under18").
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("under18"));
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testViewUsersFromBulgariaByAuthenticatedUser_Success() throws Exception {
        mockMvc.perform(get("/fromBulgaria").
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("fromBulgaria"));
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testDeleteUserProfileByOwner_Success() throws Exception {
        mockMvc.perform(delete("/delete/{id}", testUser.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection()).
                andExpect(view().name("redirect:/"));
    }

    @WithUserDetails(value = "admin",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testDeleteUserProfileByAdmin_Success() throws Exception {
        mockMvc.perform(delete("/delete/{id}", testUser.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection()).
                andExpect(view().name("redirect:/"));
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testDeleteUserProfileByNotOwnerAndNotAdmin_Forbidden() throws Exception {
        mockMvc.perform(delete("/delete/{id}", testAdmin.getId()).
                        with(csrf())
                )
                .andExpect(status().isForbidden())
                .andExpect(view().name("error/403"));
    }

    @Test
    void testDeleteUserProfileByAnonymous_Forbidden() throws Exception {
        mockMvc.perform(delete("/delete/{id}", testUser.getId()).
                        with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andReturn();
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testSearchQueryWithValidDataByAuthenticatedUser_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/search")
                        .param("country", "Bulgaria")
                        .param("maxAge", "22")
                        .param("minAge", "20")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("searchUserDto"))
                .andExpect(model().attributeExists("criteria"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", Matchers.hasSize(1)));
    }
}