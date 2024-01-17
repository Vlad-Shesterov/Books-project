package com.example.books.service;


import com.example.books.entity.Book;
import com.example.books.entity.User;
import com.example.books.entity.UserAction;
import com.example.books.repository.UserActionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserActionService {

    private UserActionRepository userActionRepository;

    @Autowired
    private UserService userService;


    private enum ActionType {
        ADD,
        REMOVE,
        EDIT
    }

    public List<UserAction> getAll() {
        return userActionRepository.findAll();
    }

    public void addAction(Book book, User user) {
        saveAction(ActionType.ADD.name(), book, user);
    }

    public void deleteAction(Book book, User user) {
        saveAction(ActionType.REMOVE.name(), book, user);
    }

    public void editAction(Book book, User user) {
        saveAction(ActionType.EDIT.name(), book, user);
    }

    private void saveAction(String action, Book book, User user) {
        UserAction userAction = new UserAction();
        userAction.setDescription(action + " " + book.toString());
//        userAction.setUserId(user);
        userAction.setUserId(userService.getCurrentUser());
        userAction.setDateActions(new Date(System.currentTimeMillis()));
        userActionRepository.save(userAction);
    }


}
