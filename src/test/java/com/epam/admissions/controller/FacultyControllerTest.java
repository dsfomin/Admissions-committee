package com.epam.admissions.controller;

import com.epam.admissions.service.FacultyRegistrationService;
import com.epam.admissions.service.FacultyService;
import com.epam.admissions.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/create-faculty-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql", "/create-faculty-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    FacultyService facultyService;

    @MockBean
    FacultyRegistrationService facultyRegistrationService;

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "ADMIN", "USER" })
    public void editFaculty() throws Exception {
        this.mockMvc.perform(get("/faculty/edit/{faculty}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors());
    }

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "ADMIN", "USER" })
    public void deleteFaculty() throws Exception {
        this.mockMvc.perform(get("/faculty/delete/{faculty}", 2))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/faculty?pageNo=0&pageSize=5&sortBy=name&order=asc"));

        verify(facultyService, times(1)).deleteById(2L);
    }

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "ADMIN", "USER" })
    public void facultyPage() throws Exception {
        this.mockMvc.perform(get("/faculty/{faculty}", 2))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "ADMIN", "USER" })
    public void facultySave() throws Exception {
        this.mockMvc.perform(post("/faculty")
                .param("facultyId", "4")
                .param("name", "Faculty04")
                .param("contractPlaces", "2")
                .param("budgetPlaces", "3"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "dsfomin2@gmail.com", authorities = { "ADMIN", "USER" })
    public void facultyAdd() throws Exception {
        this.mockMvc.perform(get("/faculty/add"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}