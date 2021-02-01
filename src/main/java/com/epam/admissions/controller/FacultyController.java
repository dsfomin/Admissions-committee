package com.epam.admissions.controller;

import com.epam.admissions.entity.Faculty;
import com.epam.admissions.entity.User;
import com.epam.admissions.service.FacultyService;
import com.epam.admissions.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Controller
@RequestMapping("/faculty")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
@AllArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;
    private final UserService userService;

    private static Integer pageNo = 0;
    private static Integer pageSize = 10;
    private static String sortBy = "name";
    private static String order = "asc";


    @GetMapping
    public String userList(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @NonNull Model model
    ) {
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("facultiesPage", facultyService.findAll(pageNo, pageSize, order, sortBy));

        FacultyController.pageNo = pageNo;
        FacultyController.pageSize = pageSize;
        FacultyController.sortBy = sortBy;
        FacultyController.order = order;

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

    @GetMapping("{faculty}")
    public String facultyPage(@PathVariable Faculty faculty,
                              @AuthenticationPrincipal User user,
                              @NonNull Model model) {

        User userFromDb = userService.findByEmail(user.getEmail()).orElseThrow(() ->
                new IllegalStateException("user with such name not found"));

        model.addAttribute("faculty", faculty);

        boolean alreadyParticipate = userFromDb.getSelectedFaculties()
                .stream()
                .map(Faculty::getName)
                .anyMatch(faculty.getName()::equals);

        model.addAttribute("alreadyParticipate", alreadyParticipate);

        return "facultyPage";
    }

    @PostMapping
    public String facultySave(
            @RequestParam String name,
            @RequestParam Integer contractPlaces,
            @RequestParam Integer budgetPlaces,
            @RequestParam ("facultyId") @NonNull Faculty faculty
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

    @PostMapping("/add")
    public String addFaculty(@NonNull Faculty faculty, Model model) {
        Optional<Faculty> facultyFromDb = facultyService.findByName(faculty.getName());

        if (facultyFromDb.isPresent()) {
            model.addAttribute("message", "Faculty with such name already exists!");
            return "addFaculty";
        }

        faculty.setFinalized(false);
        facultyService.save(faculty);

        return "redirect:/faculty";
    }

    @GetMapping("/{faculty}/participate")
    public String participateFaculty(@PathVariable Faculty faculty,
                                     @AuthenticationPrincipal User user) {
        userService.participate(user, faculty);

        return "redirect:/faculty/" + faculty.getId();
    }
}
