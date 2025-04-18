package com.library.library.controller;

import com.library.library.dto.AppUserDTO;
import com.library.library.model.AppUser;
import com.library.library.service.appUser.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private AppUserService appUserService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/testa")
    public String testa() {
        return "testa";
    }

    @PostMapping
    public ResponseEntity<AppUserDTO> addUser(@RequestBody AppUserDTO userDTO) {
        AppUserDTO savedUser = appUserService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
