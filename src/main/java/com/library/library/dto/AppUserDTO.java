package com.library.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String mobile;
}
