package com.example.books.controller;

import com.example.books.entity.Role;
import com.example.books.entity.User;
import com.example.books.entity.UserRole;
import com.example.books.repository.RoleRepository;
import com.example.books.repository.UserRepository;
import com.example.books.service.SecurityService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SecurityService securityService;

    @PersistenceContext
    private EntityManager entityManager;


    @GetMapping("/users/list")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users/list";
    }

    @PostMapping("/users")
    public String addRoles(@RequestParam("userId") Long userId, @RequestParam("role") String role, Model model) {


        // Получаем текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, что текущий пользователь имеет роль ADMIN
        boolean isAdmin = securityService.isUserInRole("ROLE_ADMIN");

        if (!isAdmin) {
            throw new AccessDeniedException("У вас недостаточно прав для выполнения этого действия.");
        }

        
        
        // Проверяем, что роль существует
        Role roleToAdd = roleRepository.findByName(String.valueOf(role));
        // Выводим роль

        System.out.println(role);
        System.out.println(userId);
        if (roleToAdd == null) {
            model.addAttribute("error", "Роль не существует.");
            return "users"; // Вернуться к списку пользователей с ошибкой
        }


        // Получаем пользователя по его идентификатору
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));
                // Добавляем роль пользователю
        System.out.println(user);
        Set<UserRole> userRoles = user.getUserRoles();
        userRoles.add(new UserRole(user, roleToAdd));

        // Сохраняем изменения в базе данных
        entityManager.flush();

        return "redirect:/users"; // Перенаправляем на список пользователей
    }
}