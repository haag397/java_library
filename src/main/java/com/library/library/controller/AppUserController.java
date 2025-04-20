package com.library.library.controller;

import com.library.library.dto.book.BookRequestDTO;
import com.library.library.dto.user.AppUserDTO;
import com.library.library.dto.user.AppUserMapper;
import com.library.library.dto.user.UserCreationRequestDTO;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.AppUser;
import com.library.library.service.appUser.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;
    public AppUserController(AppUserService appUserService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.appUserMapper = appUserMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/testa")
    public String testa() {
        return "testa";
    }

    @PostMapping(("/create"))
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserCreationRequestDTO userCreationRequestDTO) {
        UserResponseDTO savedUser= appUserService.createUser(userCreationRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/username")
    public ResponseEntity<UserResponseDTO> findByUserName(@RequestParam String userName) {
        AppUser user = appUserService.findByUserName(userName).
                orElseThrow(() -> new UserNotFoundException("user not found with username" + userName));
       UserResponseDTO responseDTO = appUserMapper.toUserResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/email")
    public AppUser findByEmail(@RequestBody String email) {
        return appUserService.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("user not found with email " + email));
    }

    @PostMapping
    public ResponseEntity<AppUserDTO> addBook(@RequestBody BookRequestDTO bookDTO) {return null;}
}
