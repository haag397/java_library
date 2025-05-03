package com.library.library.service.User;

import com.library.library.dto.auth.RegisterRequestDTO;
import com.library.library.dto.user.UserMapper;
import com.library.library.dto.user.UserCreationRequestDTO;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.dto.user.UserUpdateRequestDTO;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.User;
import com.library.library.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public UserResponseDTO updateUser(UUID id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        userUpdateRequestDTO.email().ifPresent(user::setEmail);
        userUpdateRequestDTO.firstName().ifPresent(user::setFirstName);
        userUpdateRequestDTO.lastName().ifPresent(user::setLastName);
        userUpdateRequestDTO.mobile().ifPresent(user::setMobile);
        userUpdateRequestDTO.password().ifPresent(user::setPassword);
        userUpdateRequestDTO.role().ifPresent(user::setRole);

        userRepository.save(user);
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
