package com.epam.admissions.controller;

import com.epam.admissions.entity.Faculty;
import com.epam.admissions.entity.FacultyRegistration;
import com.epam.admissions.entity.User;
import com.epam.admissions.service.FacultyRegistrationService;
import com.epam.admissions.service.FacultyService;
import com.epam.admissions.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/faculty")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
@AllArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;
    private final UserService userService;
    private final FacultyRegistrationService facultyRegistrationService;

    private static Integer pageNo = 0;
    private static Integer pageSize = 8;
    private static String sortBy = "name";
    private static String order = "asc";

    @GetMapping
    public String userList(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "8") Integer pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @NonNull Model model
    ) {
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("facultiesPage", facultyService.findAll(pageNo, pageSize, order, sortBy));

        FacultyController.savePaginationParams(pageNo, pageSize, sortBy, order);

        return "facultyList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit/{faculty}")
    public String facultyEditForm(@PathVariable Faculty faculty, @NonNull Model model) {
        model.addAttribute("faculty", faculty);

        return "facultyEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{faculty}")
    public String deleteFaculty(@PathVariable @NonNull Faculty faculty, Model model) {
        facultyService.deleteById(faculty.getId());

        return "redirect:/faculty?pageNo=" + pageNo + "&pageSize="
                + pageSize + "&sortBy=" + sortBy + "&order=" + order;
    }

    @Transactional
    @GetMapping("{faculty}")
    public String facultyPage(@PathVariable Faculty faculty,
                              @AuthenticationPrincipal User user,
                              @NonNull Model model) {

        User userFromDb = userService.findByEmail(user.getEmail());

        model.addAttribute("faculty", faculty);
        model.addAttribute("alreadyParticipate", isUserAlreadyParticipate(userFromDb, faculty));
        model.addAttribute("usersTop",
                getTopUsersByNotes(facultyRegistrationService.findAllFacultyRegistrations(faculty)));

        return "facultyPage";
    }

    @PostMapping
    public String facultySave(
            @RequestParam String name,
            @RequestParam Integer contractPlaces,
            @RequestParam Integer budgetPlaces,
            @RequestParam("facultyId") @NonNull Faculty faculty
    ) {
        faculty.setName(name);
        faculty.setContractPlaces(contractPlaces);
        faculty.setBudgetPlaces(budgetPlaces);

        facultyService.save(faculty);

        return "redirect:/faculty";
    }

    @GetMapping("/add")
    public String addFaculty() {
        return "addFaculty";
    }

    @Transactional
    @PostMapping("/add")
    public String addFaculty(@NonNull Faculty faculty, Model model) {

        if (facultyService.isFacultyAlreadyExists(faculty)) {
            model.addAttribute("message", "Faculty with such name already exists!");
            return "addFaculty";
        }

        faculty.setFinalized(false);
        facultyService.save(faculty);

        return "redirect:/faculty";
    }

    @PreAuthorize("hasAuthority('USER') && !hasAuthority('ADMIN')")
    @PostMapping("/{faculty}/participate")
    public String participateFaculty(@PathVariable Faculty faculty,
                                     @RequestParam Double note1,
                                     @RequestParam Double note2,
                                     @RequestParam Double note3,
                                     @AuthenticationPrincipal User user) {
        FacultyRegistration facultyRegistration = FacultyRegistration.builder()
                .faculty(faculty)
                .user(user)
                .notes(List.of(note1, note2, note3))
                .build();

        facultyRegistrationService.saveFacultyRegistration(facultyRegistration);

        return "redirect:/faculty/" + faculty.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{faculty}/finalize")
    public String finalizeFaculty(@PathVariable Faculty faculty) {
        facultyService.finalizeFaculty(faculty);
        return "redirect:/faculty/" + faculty.getId();
    }

    private static void savePaginationParams(Integer pageNo, Integer pageSize, String sortBy, String order) {
        FacultyController.pageNo = pageNo;
        FacultyController.pageSize = pageSize;
        FacultyController.sortBy = sortBy;
        FacultyController.order = order;
    }

    private Boolean isUserAlreadyParticipate(User user, Faculty faculty) {
        return user.getSelectedFaculties()
                .stream()
                .map(FacultyRegistration::getFaculty)
                .map(Faculty::getName)
                .anyMatch(faculty.getName()::equals);
    }

    private Set<User> getTopUsersByNotes(List<FacultyRegistration> facultyRegistrations) {
        return facultyRegistrations.stream()
                .sorted(Comparator
                        .comparingDouble(FacultyRegistration::getAverageExamNote)
                        .thenComparing(FacultyRegistration::getUserAverageSchoolNote)
                        .reversed())
                .map(FacultyRegistration::getUser)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
