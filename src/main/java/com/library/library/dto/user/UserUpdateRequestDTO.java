package com.library.library.dto.user;

import com.library.library.model.Role;
import lombok.Builder;

import java.io.Serializable;
import java.util.Optional;

@Builder
public record UserUpdateRequestDTO(
        Optional<String> email,
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<String> mobile,
        Optional<String> password,
        Optional<Role> role){}
