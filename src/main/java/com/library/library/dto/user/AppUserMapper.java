package com.library.library.dto.user;

import com.library.library.model.AppUser;
import org.apache.catalina.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    // Entity to response dto
    UserResponseDTO toUserResponseDTO(AppUser appUser);
    //request dto to entity for saving db
    AppUser toEntity(UserCreationRequestDTO userCreationRequestDTO);
}
