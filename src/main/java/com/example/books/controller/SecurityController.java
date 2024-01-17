package com.example.books.controller;


import com.example.books.dto.UserDto;
import com.example.books.entity.Role;
import com.example.books.entity.User;
import com.example.books.service.UserService;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
public class SecurityController {

    private UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @GetMapping("/redirect")
    public String redirect() {
        List<Role> userRoles = userService.getCurrentUser().getRoles();
        for (Role role : userRoles) {
            if (role.getName().equals("ROLE_ADMIN")) {
                return "redirect:/users";
            }

            if (role.getName().equals("ROLE_USER")) {
                return "redirect:/list";
            }

            if (role.getName().equals("READ_ONLY")) {
                return "redirect:/list1";
            }
        }
        throw new RuntimeException("Роль не найдена");
    }

    @PostMapping("/admin/update-roles")
    public String updateRoles(@RequestParam String username, @RequestParam List<String> roles) {
        // Ваш код для обновления ролей пользователя в базе данных
        return "redirect:/admin/manage-roles";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null & !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "На этот email уже зарегистрирована учетная запись.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }
        userService.saveUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}