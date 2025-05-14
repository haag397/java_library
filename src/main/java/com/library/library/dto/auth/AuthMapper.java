package com.library.library.dto.auth;

import com.library.library.command.RegisterUserCommand;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    User toEntity(RegisterRequestDTO registerRequestDTO);
    UserResponseDTO toUserResponseDTO(User users);
    RegisterUserCommand toCommand(RegisterRequestDTO registerRequestDTO);
}
