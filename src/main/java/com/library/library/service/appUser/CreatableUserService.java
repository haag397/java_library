package com.library.library.service.appUser;

import com.library.library.dto.user.UserCreationRequestDTO;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.model.AppUser;

import java.util.Optional;

public interface CreatableUserService {
    UserResponseDTO createUser(UserCreationRequestDTO userCreationRequestDTO);
//    Optional<AppUser> findByUserName(String userName);
    Optional<AppUser> findByEmail(String email);

}
