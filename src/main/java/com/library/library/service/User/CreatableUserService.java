package com.library.library.service.User;

import com.library.library.dto.user.UserCreationRequestDTO;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.dto.user.UserUpdateRequestDTO;
import com.library.library.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreatableUserService {
    UserResponseDTO updateUser(UUID id, UserUpdateRequestDTO userUpdateRequest);
    void deleteUser(UUID id);
    UserResponseDTO findById(UUID id);
    List<UserResponseDTO> findAll();
}
