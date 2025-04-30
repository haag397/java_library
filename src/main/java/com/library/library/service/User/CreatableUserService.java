package com.library.library.service.User;

import com.library.library.dto.user.UserCreationRequestDTO;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.model.User;

import java.util.Optional;

public interface CreatableUserService {
    UserResponseDTO createUser(UserCreationRequestDTO userCreationRequestDTO);
//    Optional<AppUser> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
