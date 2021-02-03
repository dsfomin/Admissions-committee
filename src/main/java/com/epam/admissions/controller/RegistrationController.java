package com.epam.admissions.controller;

import com.epam.admissions.entity.User;
import com.epam.admissions.entity.UserRole;
import com.epam.admissions.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller
@AllArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@NonNull User user, Model model) {
        Optional<User> userFromDb = userService.findByEmail(user.getEmail());

        if (userFromDb.isPresent()) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Set.of(UserRole.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.saveUser(user);
        return "redirect:/login";
    }
}