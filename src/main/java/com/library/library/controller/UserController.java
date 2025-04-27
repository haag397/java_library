package com.library.library.controller;

//import com.library.library.dto.book.BookRequestDTO;
import com.library.library.config.JwtService;
import com.library.library.dto.user.UserMapper;
import com.library.library.dto.user.UserCreationRequestDTO;
import com.library.library.dto.user.UserResponseDTO;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.User;
import com.library.library.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsService userDetailsService;

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @PostMapping(("/create"))
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserCreationRequestDTO userCreationRequestDTO) {
        UserResponseDTO savedUser= userService.createUser(userCreationRequestDTO);
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/check-role")
    public String checkRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        return "User roles: " + roles;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/role")
    public ResponseEntity<String> getRole(@RequestHeader("Authorization") String authHeader) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaa");
        String token = authHeader.substring(7); // Remove "Bearer "
        String role = jwtService.extractRole(token);
        return ResponseEntity.ok(role);
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

//    @GetMapping("/email")
//    public ResponseEntity<UserResponseDTO>  findByEmail(@RequestParam String email) {
//        User user = userService.findByEmail(email).
//                orElseThrow(() -> new UserNotFoundException("user not found with email " + email));
//        UserResponseDTO responseDTO = userMapper.toUserResponseDTO(user);
//        return ResponseEntity.ok(responseDTO);
//    }

}
