package com.epam.admissions.service;

import com.epam.admissions.entity.*;
import com.epam.admissions.repository.FacultyRegistrationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class FacultyRegistrationServiceTest {
    @Autowired
    FacultyRegistrationService facultyRegistrationService;

    @MockBean
    FacultyRegistrationRepository facultyRegistrationRepository;

    @BeforeEach
    void setUp() {
        Faculty faculty1 = new Faculty(1l, "Faculty1", 1, 1,
                Set.of(), Collections.singleton(Subject.BIOLOGY), false);
        Faculty faculty2 = new Faculty(2l, "Faculty2", 2, 2,
                Set.of(), Collections.singleton(Subject.CHEMISTRY), false);

        User user1 = new User(1l, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());
        User user2 = new User(1l, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());

        FacultyRegistration facultyRegistration = new FacultyRegistration(1L, user1, faculty1, Map.of(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Test save facultyRegistration")
    void testSave() {
        Faculty faculty1 = new Faculty(1L, "Faculty1", 1, 1,
                Set.of(), Collections.singleton(Subject.BIOLOGY), false);
        User user1 = new User(1L, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());
        FacultyRegistration facultyRegistration = new FacultyRegistration(1L, user1, faculty1, Map.of(), LocalDateTime.now());

        // Setup our mock repository
        doReturn(facultyRegistration).when(facultyRegistrationRepository).save(any());

        // Execute the service call
        FacultyRegistration returnedFacultyRegistration = facultyRegistrationService.saveFacultyRegistration(facultyRegistration);

        // Assert the response
        assertNotNull(returnedFacultyRegistration, "The saved faculty registration should not be null");
        assertEquals(facultyRegistration, returnedFacultyRegistration, "Faculty registrations should be equal");
        assertEquals(user1, returnedFacultyRegistration.getUser(), "Users should be same");
        assertEquals(faculty1, returnedFacultyRegistration.getFaculty(), "Faculties should be same");
    }

    @Test
    @DisplayName("Test findFacultyRegistration facultyRegistration")
    void testFindByName() {
        // Setup our mock repository
        Faculty faculty1 = new Faculty(1L, "Faculty1", 1, 1,
                Set.of(), Collections.singleton(Subject.BIOLOGY), false);
        User user1 = new User(1L, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());
        FacultyRegistration facultyRegistration = new FacultyRegistration(1L, user1, faculty1, Map.of(), LocalDateTime.now());

        doReturn(Optional.of(facultyRegistration)).when(facultyRegistrationRepository).findByFacultyAndUser(faculty1, user1);

        // Execute the service call
        FacultyRegistration returnedFacultyRegistration = facultyRegistrationService.findFacultyRegistration(faculty1, user1);

        // Assert the response
        assertNotNull(returnedFacultyRegistration, "The saved faculty should not be null");
        assertEquals(facultyRegistration, returnedFacultyRegistration, "Faculty registrations should be equal");
        assertEquals(user1, returnedFacultyRegistration.getUser(), "Users should be same");
        assertEquals(faculty1, returnedFacultyRegistration.getFaculty(), "Faculties should be same");
    }

    @Test
    @DisplayName("Test findFacultyRegistration Not Found")
    void testFindByIdNotFound() {
        // Setup our mock repository
        Faculty faculty1 = new Faculty(1L, "Faculty1", 1, 1,
                Set.of(), Collections.singleton(Subject.BIOLOGY), false);
        User user1 = new User(1L, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());
        doReturn(Optional.empty()).when(facultyRegistrationRepository).findByFacultyAndUser(faculty1, user1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Execute the service call
            facultyRegistrationService.findFacultyRegistration(faculty1, user1);
        });

        assertEquals("Unknown faculty registration", exception.getMessage());

    }

    @Test
    @DisplayName("Test findAll by user")
    void testFindAllUsers() {
        // Setup our mock repository
        Faculty faculty1 = new Faculty(1l, "Faculty1", 1, 1,
                Set.of(), Collections.singleton(Subject.BIOLOGY), false);
        Faculty faculty2 = new Faculty(2l, "Faculty2", 2, 2,
                Set.of(), Collections.singleton(Subject.CHEMISTRY), false);

        User user1 = new User(1l, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());

        FacultyRegistration facultyRegistration1 = new FacultyRegistration(1L, user1, faculty1, Map.of(), LocalDateTime.now());
        FacultyRegistration facultyRegistration3 = new FacultyRegistration(3L, user1, faculty2, Map.of(), LocalDateTime.now());


        doReturn(Arrays.asList(facultyRegistration1, facultyRegistration3)).when(facultyRegistrationRepository).findByUser(user1);

        // Execute the service call
        List<FacultyRegistration> facultyRegistrations = facultyRegistrationService.findAllFacultyRegistrations(user1);

        // Assert the response
        Assertions.assertEquals(2, facultyRegistrations.size(), "findAll should return 2 facultyRegistrations");
    }

    @Test
    @DisplayName("Test findAll by faculty")
    void testFindAllByFaculty() {
        // Setup our mock repository
        Faculty faculty1 = new Faculty(1l, "Faculty1", 1, 1,
                Set.of(), Collections.singleton(Subject.BIOLOGY), false);

        User user1 = new User(1l, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());
        User user2 = new User(1l, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());

        FacultyRegistration facultyRegistration1 = new FacultyRegistration(1L, user1, faculty1, Map.of(), LocalDateTime.now());
        FacultyRegistration facultyRegistration3 = new FacultyRegistration(3L, user2, faculty1, Map.of(), LocalDateTime.now());


        doReturn(Arrays.asList(facultyRegistration1, facultyRegistration3)).when(facultyRegistrationRepository).findByFaculty(faculty1);

        // Execute the service call
        List<FacultyRegistration> facultyRegistrations = facultyRegistrationService.findAllFacultyRegistrations(faculty1);

        // Assert the response
        Assertions.assertEquals(2, facultyRegistrations.size(), "findAll should return 2 facultyRegistrations");
    }
}