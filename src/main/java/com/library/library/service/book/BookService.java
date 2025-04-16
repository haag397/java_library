package com.library.library.service.book;

import com.library.library.model.Book;
import com.library.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class BookService implements CreatableService{
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void createBook(Book book) {
    }

    @Override
    public Optional<Book> findByBookTitle(String bookTitle) {
        return Optional.empty();
    }

    @Override
    public Optional<Book> findById(Long aLong) {
        return Optional.empty();
    }
}
