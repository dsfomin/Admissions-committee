package com.epam.admissions.controller;

import com.epam.admissions.entity.User;
import com.epam.admissions.entity.UserRole;
import com.epam.admissions.service.FacultyRegistrationService;
import com.epam.admissions.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final FacultyRegistrationService facultyRegistrationService;

    @GetMapping
    public String userList(@NonNull Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("adminRole", UserRole.ADMIN);

        return "userList";
    }

    @GetMapping("/block/{user}")
    public String userBlock(@PathVariable @NonNull User user, Model model) {
        userService.blockUser(user.getId());

        return "redirect:/user";
    }

    @GetMapping("/unblock/{user}")
    public String userUnblock(@PathVariable @NonNull User user, Model model) {
        userService.unblockUser(user.getId());

        return "redirect:/user";
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/profile")
    public String myProfile(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("facultyRegistration", facultyRegistrationService.findAllFacultyRegistrations(user));
        model.addAttribute("user", user);
        model.addAttribute("myAdminProfile", user.getRoles().contains(UserRole.ADMIN));

        return "userProfile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/profile/{user}")
    public String userProfile(@PathVariable User user, Model model) {

        model.addAttribute("facultyRegistration", facultyRegistrationService.findAllFacultyRegistrations(user));
        model.addAttribute("user", user);
        model.addAttribute("myAdminProfile", false);

        return "userProfile";
    }
}
