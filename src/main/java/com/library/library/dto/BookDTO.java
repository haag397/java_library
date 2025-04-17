package com.library.library.dto;

import java.math.BigDecimal;
import java.util.List;

public class BookDTO {
    private Long id;
    private String book_title;
    private BigDecimal price;
    private String publisher;
    private int quantity;
    private List<AuthorDTO> authors;
    private List<CategoryDto> categories;
}

