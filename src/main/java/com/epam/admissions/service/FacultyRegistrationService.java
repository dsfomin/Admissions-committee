package com.epam.admissions.service;

import com.epam.admissions.entity.Faculty;
import com.epam.admissions.entity.FacultyRegistration;
import com.epam.admissions.entity.User;
import com.epam.admissions.repository.FacultyRegistrationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FacultyRegistrationService {

    private final FacultyRegistrationRepository facultyRegistrationRepository;

    public FacultyRegistration saveFacultyRegistration(FacultyRegistration facultyRegistration) {
        return facultyRegistrationRepository.save(facultyRegistration);
    }

    public FacultyRegistration findFacultyRegistration(Faculty faculty, User user) {
        return facultyRegistrationRepository.findByFacultyAndUser(faculty, user).orElseThrow(
                () -> new IllegalStateException("Unknown faculty registration")
        );
    }

    public List<FacultyRegistration> findAllFacultyRegistrations(Faculty faculty) {
        return facultyRegistrationRepository.findByFaculty(faculty);
    }

    public List<FacultyRegistration> findAllFacultyRegistrations(User user) {
        return facultyRegistrationRepository.findByUser(user);
    }
}
