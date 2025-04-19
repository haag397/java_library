package com.library.library.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequestDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
    private String password;
    private String username;
}

