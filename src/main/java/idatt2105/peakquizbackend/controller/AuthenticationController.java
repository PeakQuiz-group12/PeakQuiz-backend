package idatt2105.peakquizbackend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import idatt2105.peakquizbackend.exceptions.UserAlreadyExistsException;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.UserService;
import idatt2105.peakquizbackend.service.AuthService;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
public class AuthenticationController {

    public static final String keyStr = "testsecrettestsecrettestsecrettestsecrettestsecret";
    private final UserService userService;

    private final AuthService authService;

    public AuthenticationController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @CrossOrigin
    public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String password,
            @RequestParam String mail) {

        if (userService.usernameExists(username)) {
            throw new UserAlreadyExistsException();
        }

        if (!isEmailValid(mail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }

        if (!isPasswordStrong(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Password must be at least 8 characters long, include numbers, upper and lower case letters, and at least one special character");
        }

        if (!authService.isPasswordStrong(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Password must be at least 8 characters long, include numbers, upper and lower case letters, and at least one special character");
        }

        String encodedPassword = authService.encryptPassword(password);

        userService.saveUser(new User(username, mail, encodedPassword));
        System.out.println("New user registered");

        String accessToken = authService.generateToken(username, Duration.ofMinutes(5), keyStr);
        String refreshToken = authService.generateToken(username, Duration.ofMinutes(30), keyStr);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
    }

    private boolean isEmailValid(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

    private boolean isPasswordStrong(String password) {
        // Example criteria: at least 8 characters, including numbers, letters and at least one special character
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }

    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {

        User user = userService.findUserByUsername(username);

        String encodedPassword = user.getPassword();

        if (authService.matches(password, encodedPassword)) {
            String accessToken = authService.generateToken(username, Duration.ofMinutes(5), keyStr);
            String refreshToken = authService.generateToken(username, Duration.ofMinutes(30), keyStr);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username or password");
        }
    }

    @PostMapping("/refreshToken")
    @CrossOrigin
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(keyStr);
            JWTVerifier verifier = JWT.require(algorithm).build(); // Reuse the JWTVerifier
            DecodedJWT jwt = verifier.verify(refreshToken); // Verify the passed refresh token
            String userId = jwt.getSubject();
            System.out.println(userId);

            // Assuming the refresh token is valid, issue a new access token
            String newAccessToken = authService.generateToken(userId, Duration.ofMinutes(5), keyStr);
            System.out.println("newAccessToken: " + newAccessToken);

            return ResponseEntity.ok(newAccessToken);
        } catch (JWTVerificationException exception) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @GetMapping("/validate-token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> validateToken() {
        // If this point is reached, the JWTAuthorizationFilter has already validated the token
        return ResponseEntity.ok().body("Token is valid");
    }
}
