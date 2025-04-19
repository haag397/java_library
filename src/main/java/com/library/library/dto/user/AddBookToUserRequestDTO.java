package com.library.library.dto.user;

import com.library.library.dto.book.BookRequestDTO;
import com.library.library.dto.book.BookResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBookToUserRequestDTO {
    private String email;
    private String mobile;
    private String username;
    private List<BookRequestDTO> books;
}
