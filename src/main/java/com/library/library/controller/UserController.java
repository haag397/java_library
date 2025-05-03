package com.library.library.controller;

import com.library.library.dto.user.UserUpdateRequestDTO;
import com.library.library.exception.UserExistException;
import com.library.library.exception.UserNotFoundException;
import com.library.library.model.User;
import com.library.library.repository.UserRepository;

import com.library.library.dto.user.UserResponseDTO;
import com.library.library.service.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/check-role")
    public String checkRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        return "User roles: " + roles;
    }

    @GetMapping("/getUser")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> getUser(
            @RequestParam("id") UUID id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        UserResponseDTO userResponseDTO = userService.findById(user.getId());
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) UUID id) {
        if (id != null) {
            return userRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            List<UserResponseDTO> users = userService.findAll();
            return ResponseEntity.ok(users);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> updateUser(
            @RequestParam("id") UUID id,
            @RequestBody @Valid UserUpdateRequestDTO userUpdateRequestDTO) {

        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    //
    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteUser(
            @RequestParam("id") UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        } catch (UserExistException e) {
            throw new UserNotFoundException();
        }
    }
}


