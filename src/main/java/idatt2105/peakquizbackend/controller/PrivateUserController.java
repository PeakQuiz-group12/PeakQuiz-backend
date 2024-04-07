package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.mapper.UserMapper;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me")
@AllArgsConstructor
public class PrivateUserController {

    UserService userService;

    @GetMapping
    public ResponseEntity<User> getMe(Authentication authentication) {
        return ResponseEntity.ok(userService.findUserByUsername(authentication.getName()));
    }

    // todo: Bug - the logged in user is no longer you, because username differs
    @PutMapping
    public ResponseEntity<UserDTO> updateMe(Authentication authentication, @RequestBody UserDTO userDTO) {
        User user = userService.findUserByUsername(authentication.getName());

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }

        UserDTO updatedUserDTO = UserMapper.INSTANCE.toDTO(userService.saveUser(user));

        // SecurityContextHolder.getContext().setAuthentication(auth);
        return ResponseEntity.ok(updatedUserDTO);
    }
}
