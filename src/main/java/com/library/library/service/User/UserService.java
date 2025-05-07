package com.library.library.service.User;

import com.library.library.dto.user.UserMapper;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.dto.user.UserRequestDTO;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.User;
import com.library.library.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements CreatableUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Cacheable(value = "userCache", key = "'user_' + #id")
    public UserResponseDTO findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(userMapper::toUserResponseDTO).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (userRequestDTO.email() != null) user.setEmail(userRequestDTO.email());
        if (userRequestDTO.firstName() != null) user.setFirstName(userRequestDTO.firstName());
        if (userRequestDTO.lastName() != null) user.setLastName(userRequestDTO.lastName());
        if (userRequestDTO.password() != null) user.setPassword(userRequestDTO.password());
        if (userRequestDTO.role() != null) user.setRole(userRequestDTO.role());
        if(userRequestDTO.mobile() != null) user.setMobile(userRequestDTO.mobile());

        userRepository.save(user);
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
