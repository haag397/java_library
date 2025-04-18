package com.library.library.service.appUser;

import com.library.library.dto.AppUserDTO;
import com.library.library.mapper.AppUserMapper;
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
    public AppUser addAppUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public AppUserDTO createUser(AppUserDTO userDTO) {
        AppUser appUser = appUserMapper.toEntity(userDTO);
        AppUser savedUser = appUserRepository.save(appUser);
        return appUserMapper.toDto(savedUser);
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
