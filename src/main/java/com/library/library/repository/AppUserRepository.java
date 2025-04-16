package com.library.library.repository;

import com.library.library.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByUserName(String userName);
    Optional<AppUser> findByEmail(String email);
}
