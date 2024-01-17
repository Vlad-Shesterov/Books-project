package com.example.books.service;

import com.example.books.entity.Book;
import com.example.books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final UserService userService;

    private final BookRepository bookRepository;


    @Override
    public List<Book> getBooks() {

        return bookRepository.findBooksByUser(userService.getCurrentUser());
    }

    @Override
    public void addBook(Book book) {
        book.setUser(userService.getCurrentUser());
        bookRepository.save(book);
    }

}
