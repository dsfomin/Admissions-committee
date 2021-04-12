package com.epam.admissions.service;

import com.epam.admissions.entity.Faculty;
import com.epam.admissions.entity.Subject;
import com.epam.admissions.repository.FacultyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class FacultyServiceTest {

    @Autowired
    FacultyService facultyService;

    @MockBean
    FacultyRepository facultyRepository;

    @Test
    @DisplayName("Test save faculty")
    void testSave() {
        // Setup our mock repository
        Faculty faculty = new Faculty(1l, "Faculty1", 1, 1,
                Set.of(), Collections.singleton(Subject.BIOLOGY), false);
        doReturn(faculty).when(facultyRepository).save(any());

        // Execute the service call
        Faculty returnedFaculty = facultyService.save(faculty);

        // Assert the response
        assertNotNull(returnedFaculty, "The saved faculty should not be null");
        assertEquals(1, (int) returnedFaculty.getBudgetPlaces(), "Budget places should equal 1");
    }

    @Test
    @DisplayName("Test save faculty failed")
    void testSaveFailed() {
        // Setup our mock repository

        // Execute the service call
        Faculty returnedFaculty = facultyService.save(null);

        // Assert the response
        assertNull(returnedFaculty, "The saved user should be null");
        verify(facultyRepository, times(0)).save(ArgumentMatchers.any(Faculty.class));
    }

    @Test
    @DisplayName("Test findByName faculty")
    void testFindByName() {
        // Setup our mock repository
        Faculty faculty = new Faculty(1l, "Faculty1", 1, 1,
                Set.of(), Collections.singleton(Subject.BIOLOGY), false);
        doReturn(Optional.of(faculty)).when(facultyRepository).findByName("Faculty1");

        // Execute the service call
        Faculty returnedFaculty = facultyService.findByName("Faculty1");

        // Assert the response
        assertNotNull(returnedFaculty, "The saved faculty should not be null");
        assertEquals(1, (int) returnedFaculty.getBudgetPlaces(), "Budget places should equal 1");
        assertEquals(faculty, returnedFaculty, "Faculties should be equal");
    }

    @Test
    @DisplayName("Test findByName faculty Not Found")
    void testFindByIdNotFound() {
        // Setup our mock repository
        doReturn(Optional.empty()).when(facultyRepository).findByName("Faculty1");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Execute the service call
            facultyService.findByName("Faculty1");
        });

        assertEquals("Faculty with name: Faculty1 not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup our mock repository
        Faculty faculty1 = new Faculty(1l, "Faculty1", 1, 1,
                Set.of(), Collections.singleton(Subject.BIOLOGY), false);
        Faculty faculty2 = new Faculty(2l, "Faculty2", 2, 2,
                Set.of(), Collections.singleton(Subject.CHEMISTRY), false);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "name"));
        Page<Faculty> page = new PageImpl<>(List.of(faculty1, faculty2));
        doReturn(page).when(facultyRepository).findAll(pageable);

        // Execute the service call
        Page<Faculty> faculties = facultyService.findAll(0, 5, "desc", "name");

        // Assert the response
        Assertions.assertEquals(2, faculties.getTotalElements(), "findAll should return 2 faculties");
    }
}