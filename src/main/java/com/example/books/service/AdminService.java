package com.example.books.service;

import com.example.books.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    public void updateRoles(String username, List<String> roles) {
        // Ваш код для обновления ролей пользователя в базе данных
    }
}