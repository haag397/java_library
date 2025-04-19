package com.library.library.mapper;

import com.library.library.dto.user.AppUserDTO;
import com.library.library.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUser toEntity(AppUserDTO dto) {
        AppUser user = new AppUser();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setPassword(dto.getPassword());
        // Handle Book mapping if needed
        return user;
    }

    public AppUserDTO toDto(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        user.setPassword(dto.getPassword());
        // Handle BookDTO mapping if needed
        return dto;
    }
}
