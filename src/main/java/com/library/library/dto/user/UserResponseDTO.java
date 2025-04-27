package com.library.library.dto.user;

//import com.library.library.dto.book.BookResponseDTO;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
//    private List<BookResponseDTO> books;
}
