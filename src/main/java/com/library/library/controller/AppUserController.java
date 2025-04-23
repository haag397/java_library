package com.library.library.controller;

//import com.library.library.dto.book.BookRequestDTO;
import com.library.library.dto.user.AppUserDTO;
import com.library.library.dto.user.AppUserMapper;
import com.library.library.dto.user.UserCreationRequestDTO;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.AppUser;
import com.library.library.service.appUser.AppUserService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AppUserController {
    private final UserDetailsService userDetailsService;

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;

    @PostMapping(("/create"))
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserCreationRequestDTO userCreationRequestDTO) {
        UserResponseDTO savedUser= appUserService.createUser(userCreationRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/testadmin")
    public String test() {
        return "admin";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/testuser")
    public String testa() {
        return "user";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("check")
    public String check() {
        return "testaaaaa";
    }

    @GetMapping("/check-role")
    public String checkRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        return "User roles: " + roles;
    }

    @GetMapping("/print-authorities")
    public String printAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Print all authorities (roles)
        authentication.getAuthorities().forEach(authority -> {
            System.out.println("Authority: " + authority.getAuthority());
        });

        // Or return them in the response
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        return "User roles: " + roles;
    }
//    @GetMapping("/username")
//    @PermitAll
//    public ResponseEntity<UserResponseDTO> findByUserName(@RequestParam String userName) {
//        AppUser user = appUserService.findByUserName(userName).
//                orElseThrow(() -> new UserNotFoundException("user not found with username:" + userName));
//       UserResponseDTO responseDTO = appUserMapper.toUserResponseDTO(user);
//        return ResponseEntity.ok(responseDTO);
//    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseDTO>  findByEmail(@RequestParam String email) {
        AppUser user = appUserService.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("user not found with email " + email));
        UserResponseDTO responseDTO = appUserMapper.toUserResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }

}
