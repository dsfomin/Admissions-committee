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
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping("/edit/{user}")
    public String userEditForm(@PathVariable User user, @NonNull Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("userRoles", user.getRoles());

        return "userEdit";
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

    @PostMapping("/edit")
    public String userSave(
            @RequestParam String email,
            @RequestParam @NonNull Map<String, String> form,
            @RequestParam ("userId") @NonNull User user
            ) {

        user.setEmail(email);

        user.getRoles().clear();
        Set<String> roles = Arrays
                .stream(UserRole.values())
                .map(UserRole::name)
                .collect(Collectors.toSet());

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(UserRole.valueOf(key));
            }
        }

        userService.saveUser(user);
        return "redirect:/user";
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("facultyRegistration", facultyRegistrationService.findAllFacultyRegistrations(user));
        model.addAttribute("user", user);

        return "userProfile";
    }
}
