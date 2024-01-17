package com.example.books.service;

import com.example.books.entity.Book;
import org.springframework.security.access.annotation.Secured;

import java.util.List;


public interface BookService {

    @Secured("ROLE_USER")
    List<Book> getBooks();

    @Secured("ROLE_USER")
    void addBook(Book book);

}
