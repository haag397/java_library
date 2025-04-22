package com.library.library.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}

