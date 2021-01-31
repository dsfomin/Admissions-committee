package com.epam.admissions.controller;

import com.epam.admissions.entity.Faculty;
import com.epam.admissions.service.FacultyService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/faculty")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
@AllArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @GetMapping
    public String userList(
                           @RequestParam(defaultValue = "0") Integer pageNo,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "name") String sortBy,
                           @RequestParam(defaultValue = "asc") String order,
                           Model model
    ) {
        model.addAttribute("facultiesPage", facultyService.findAll(pageNo, pageSize, order, sortBy));

        return "facultyList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{faculty}")
    public String userEditForm(@PathVariable Faculty faculty, Model model) {
        model.addAttribute("faculty", faculty);

        return "facultyEdit";
    }

    @PostMapping
    public String facultySave(
            @RequestParam String name,
            @RequestParam Integer contractPlaces,
            @RequestParam Integer budgetPlaces,
            @RequestParam ("facultyId") Faculty faculty
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
    public String addFaculty(Faculty faculty, Model model) {
        Optional<Faculty> facultyFromDb = facultyService.findByName(faculty.getName());

        if (facultyFromDb.isPresent()) {
            model.addAttribute("message", "Faculty with such name already exists!");
            return "addFaculty";
        }

        faculty.setFinalized(false);
        facultyService.save(faculty);

        return "redirect:/faculty";
    }
}
