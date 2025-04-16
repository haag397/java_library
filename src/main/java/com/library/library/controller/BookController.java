package com.library.library.controller;

import com.library.library.model.Book;
import com.library.library.service.book.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public void addBook(@RequestBody Book book) {
        bookService.createBook(book);
    }
}

