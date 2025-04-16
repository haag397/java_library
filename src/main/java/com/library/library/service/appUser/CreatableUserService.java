package com.library.library.service.appUser;

import com.library.library.model.AppUser;

import java.util.Optional;

public interface CreatableUserService {
    AppUser addAppUser(AppUser appUser);
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByUserName(String userName);
    Optional<AppUser> findByEmail(String email);

}
