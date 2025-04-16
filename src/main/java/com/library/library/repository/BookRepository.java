package com.library.library.repository;

import com.library.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    Optional<Book> findById(Long aLong);

    Optional<Book> findByBookTitle(String userName);


}
