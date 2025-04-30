package com.library.library.service.User;

import com.library.library.dto.user.UserMapper;
import com.library.library.dto.user.UserCreationRequestDTO;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.model.User;
import com.library.library.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class UserService implements CreatableUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreationRequestDTO userCreationRequestDTO) {
        User appUser= userMapper.toEntity(userCreationRequestDTO);
        User savedUser = userRepository.save(appUser);        // Save entity
        return userMapper.toUserResponseDTO(savedUser);
//        appUserRepository.save(users);
    }


//    @Override
//    public Optional<AppUser> findByUserName(String userName) {
//        return appUserRepository.findByUserName(userName);
//    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
