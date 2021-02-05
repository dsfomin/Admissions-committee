package com.epam.admissions.repository;

import com.epam.admissions.entity.Faculty;
import com.epam.admissions.entity.FacultyRegistration;
import com.epam.admissions.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacultyRegistrationRepository extends JpaRepository<FacultyRegistration, Long> {
    Optional<FacultyRegistration> findByFacultyAndUser(Faculty faculty, User user);

    List<FacultyRegistration> findByFaculty(Faculty faculty);

    List<FacultyRegistration> findByUser(User user);
}

