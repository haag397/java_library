package com.library.library.dto.user;

import com.library.library.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AppUserMapper {

    UserResponseDTO toUserResponseDTO(AppUser appUser);

    //request dto to entity for saving db
    AppUser toEntity(UserCreationRequestDTO userCreationRequestDTO);
}
