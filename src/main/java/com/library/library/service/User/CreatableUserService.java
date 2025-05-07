package com.library.library.service.User;

import com.library.library.dto.user.UserResponseDTO;
import com.library.library.dto.user.UserRequestDTO;

import java.util.List;
import java.util.UUID;

public interface CreatableUserService {
    UserResponseDTO updateUser(UUID id, UserRequestDTO userUpdateRequest);
    void deleteUser(UUID id);
    UserResponseDTO findById(UUID id);
    List<UserResponseDTO> findAll();
}
