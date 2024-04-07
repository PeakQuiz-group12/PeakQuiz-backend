package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.dto.UserUpdateDTO;
import idatt2105.peakquizbackend.exceptions.BadInputException;
import idatt2105.peakquizbackend.mapper.UserMapper;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.AuthService;
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

    AuthService authService;

    @GetMapping
    public ResponseEntity<UserDTO> getMe(Authentication authentication) {
        return ResponseEntity.ok(UserMapper.INSTANCE.toDTO(userService.findUserByUsername(authentication.getName())));
    }

    @PutMapping
    public ResponseEntity<String> updateMe(Authentication authentication, @RequestBody UserUpdateDTO password) {
        User user = userService.findUserByUsername(authentication.getName());

        if (password != null) {
            if (!authService.isPasswordStrong(password.getPassword())) {
                throw new BadInputException(
                        "Password must be at least 8 characters long, include numbers, upper and lower case letters, and at least one special character");
            }
            user.setPassword(authService.encryptPassword(password.getPassword()));
            System.out.println(password.getPassword());

            userService.saveUser(user);

            String message = "Updated password";
            return ResponseEntity.ok(message);

        } else {
            throw new BadInputException(
                    "Password must be at least 8 characters long, include numbers, upper and lower case letters, and at least one special character");
        }
    }
}
