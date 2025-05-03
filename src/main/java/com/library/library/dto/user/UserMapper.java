package com.library.library.dto.user;

import com.library.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    // Converts a User entity (typically from your database) to a UserResponseDTO
    UserResponseDTO toUserResponseDTO(User appUser);

    //request dto to entity for saving db
    User toEntity(UserCreationRequestDTO userCreationRequestDTO);

}
