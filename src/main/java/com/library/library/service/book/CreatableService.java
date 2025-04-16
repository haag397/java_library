package com.library.library.service.book;

import com.library.library.model.Book;

import java.util.Optional;

public interface CreatableService {
    void createBook(Book book);
    Optional<Book> findByBookTitle(String bookTitle);
    Optional<Book> findById(Long aLong);
}
