package com.epam.admissions.controller;

import com.epam.admissions.entity.User;
import com.epam.admissions.entity.UserRole;
import com.epam.admissions.repository.UserRepository;
import com.epam.admissions.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("userRoles", user.getRoles());

        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String email,
            @RequestParam Map<String, String> form,
            @RequestParam ("userId") User user
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

        userService.save(user);

        return "redirect:/user";
    }

}
