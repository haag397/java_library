package com.library.library.service.appUser;

import com.library.library.dto.user.AppUserMapper;
import com.library.library.dto.user.UserCreationRequestDTO;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.model.AppUser;
import com.library.library.repository.AppUserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class AppUserService implements CreatableUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    public List<AppUser> getAllUsers(){
        return appUserRepository.findAll();
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreationRequestDTO userCreationRequestDTO) {
        AppUser appUser= appUserMapper.toEntity(userCreationRequestDTO);
        AppUser savedUser = appUserRepository.save(appUser);        // Save entity
        return appUserMapper.toUserResponseDTO(savedUser);
//        appUserRepository.save(appUser);
    }


    @Override
    public Optional<AppUser> findByUserName(String userName) {
        return appUserRepository.findByUserName(userName);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

}
