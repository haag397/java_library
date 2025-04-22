package com.library.library.dto.user;

//import com.library.library.dto.book.BookResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
//    private List<BookResponseDTO> books;
}
