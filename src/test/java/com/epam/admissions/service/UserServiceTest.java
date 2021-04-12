package com.epam.admissions.service;

import com.epam.admissions.entity.User;
import com.epam.admissions.entity.UserRole;
import com.epam.admissions.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;


    @Test
    @DisplayName("Test save user")
    void testSave() {
        // Setup our mock repository
        User user = new User(1l, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());
        doReturn(user).when(userRepository).save(any());

        // Execute the service call
        User returnedUser = userService.saveUser(user);

        // Assert the response
        assertNotNull(returnedUser, "The saved user should not be null");
        assertEquals(10, returnedUser.getAverageSchoolNote().intValue(), "Avrg note should equal 10");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Test save user failed")
    void testSaveFailed() {
        // Setup our mock repository

        // Execute the service call
        User returnedUser = userService.saveUser(null);

        // Assert the response
        assertNull(returnedUser, "The saved user should be null");
        verify(userRepository, times(0)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Test findByEmail user")
    void testFindByEmail() {
        // Setup our mock repository
        User user = new User(1l, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());
        doReturn(Optional.of(user)).when(userRepository).findByEmail("email@mail.ru");

        // Execute the service call
        User returnedUser = userService.findByEmail("email@mail.ru");

        // Assert the response
        assertNotNull(returnedUser, "The saved user should not be null");
        assertEquals(10, returnedUser.getAverageSchoolNote().intValue(), "Avrg note should equal 10");
        assertEquals(user, returnedUser, "Users should be equal");

        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Test findByEmail user Not Found")
    void testFindByIdNotFound() {
        // Setup our mock repository
        doReturn(Optional.empty()).when(userRepository).findByEmail("email@mail.ru");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Execute the service call
            userService.findByEmail("email@mail.ru");
        });

        assertEquals("User with email: email@mail.ru not found", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup our mock repository
        User user1 = new User(1l, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());
        User user2 = new User(1l, "email@mail.ru", "1", true,
                10.0, Set.of(UserRole.USER), Set.of(), Map.of());
        doReturn(Arrays.asList(user1, user2)).when(userRepository).findAll();

        // Execute the service call
        List<User> users = userService.findAllUsers();

        // Assert the response
        Assertions.assertEquals(2, users.size(), "findAll should return 2 users");

        verify(userRepository, times(1)).findAll();
    }
}