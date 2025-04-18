package com.library.library.service.appUser;

import com.library.library.model.AppUser;
import com.library.library.repository.AppUserRepository;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class AppUserService implements CreatableUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> getAllUsers(){
        return appUserRepository.findAll();
    }

    @Override
    @Transactional
    public AppUser addAppUser(AppUser appUser) {
        return appUserRepository.save(appUser);
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
