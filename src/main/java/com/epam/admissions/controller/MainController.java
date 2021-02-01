package com.epam.admissions.controller;

import com.epam.admissions.entity.Message;
import com.epam.admissions.entity.User;
import com.epam.admissions.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    private final MessageRepository messageRepo;

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, @NonNull Model model) {
        List<Message> messages = (filter != null && !filter.isEmpty()) ?
                messageRepo.findByTag(filter) : messageRepo.findAll();

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            @NonNull Model model
    ) {
        messageRepo.save(new Message(text, tag, user));
        model.addAttribute("messages", messageRepo.findAll());
        return "main";
    }
}
