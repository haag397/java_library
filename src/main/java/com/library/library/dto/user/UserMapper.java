package com.library.library.dto.user;

import com.library.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toUserResponseDTO(User users);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);
}
