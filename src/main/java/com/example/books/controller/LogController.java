package com.example.books.controller;

import com.example.books.entity.UserAction;
import com.example.books.service.UserActionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class LogController {
    private UserActionService userActionService;

    @GetMapping("/logs")
    public String getLogs(Model model) {
        List<UserAction> userActions = userActionService.getAll();
        model.addAttribute("actions", userActions);
        return "logs";
    }
}
