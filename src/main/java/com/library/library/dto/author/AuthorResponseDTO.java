package com.library.library.dto.author;

import com.library.library.dto.book.BookResponseDTO;

import java.util.List;

public class AuthorResponseDTO {
    private Long id;
    private String first_name;
    private String last_name;
    private List<BookResponseDTO> books;
}
