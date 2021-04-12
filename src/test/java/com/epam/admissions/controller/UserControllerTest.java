package com.epam.admissions.controller;

import com.epam.admissions.entity.User;
import com.epam.admissions.service.FacultyRegistrationService;
import com.epam.admissions.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    FacultyRegistrationService facultyRegistrationService;

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "ADMIN", "USER" })
    public void profile() throws Exception {
        this.mockMvc.perform(get("/user/profile/{user}", 2L))
                .andDo(print())
                .andExpect(status().isOk());

        verify(facultyRegistrationService, times(1))
                .findAllFacultyRegistrations(ArgumentMatchers.any(User.class));
    }

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "ADMIN", "USER" })
    public void blockUser() throws Exception {
        this.mockMvc.perform(get("/user/block/{user}", 2))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"))
                .andExpect(view().name("redirect:/user"));

        verify(userService, times(1)).blockUser(2L);
    }

    @Test
    @WithMockUser(username = "user1", authorities = { "USER" })
    public void blockUserByUser() throws Exception {

        this.mockMvc.perform(get("/user/block/{user}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        verify(userService, times(0)).blockUser(2L);
    }

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "ADMIN", "USER" })
    public void unblockUser() throws Exception {
        this.mockMvc.perform(get("/user/unblock/{user}", 2))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"))
                .andExpect(view().name("redirect:/user"));

        verify(userService, times(1)).unblockUser(2L);
    }

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/user/profile")).andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void correctLogin() throws Exception {
        this.mockMvc.perform(formLogin().loginProcessingUrl("/signin")
                .user("dsfomin2@gmail.com").password("1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/login")
                .param("email", "Dima"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "ADMIN", "USER" })
    public void userList() throws Exception {
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("List of users")));
    }

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "USER" })
    public void userListForbiddenForUser() throws Exception {
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "USER" })
    public void mainPageAuthentication() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(content().string(containsString("dsfomin2@gmail.com")))
                .andExpect(xpath("//*[@id='navbarSupportedContent']/div[1]/a").string("dsfomin2@gmail.com"));
    }
}