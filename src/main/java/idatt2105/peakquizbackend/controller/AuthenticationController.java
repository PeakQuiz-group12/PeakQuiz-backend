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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    /**
     * Registers a new user with a username, password, and email.
     *
     * @param username The username of the new user
     * @param password The password of the new user
     * @param mail     The email of the new user
     * @return ResponseEntity containing access and refresh tokens upon successful registration
     */
    @Operation(summary = "Register user",
            description = "Register a new user with a username, password, and email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully registered user"),
                    @ApiResponse(responseCode = "400", description = "Invalid email format or weak password"),
                    @ApiResponse(responseCode = "409", description = "Username already exists") })
    @PostMapping("/register")
    @CrossOrigin
    public ResponseEntity<?> registerUser(
            @Parameter(description = "Username", example = "john_doe") @RequestParam String username,
            @Parameter(description = "Password", example = "Password@123") @RequestParam String password,
            @Parameter(description = "Email", example = "john@example.com") @RequestParam String mail) {

        if (userService.usernameExists(username)) {
            throw new UserAlreadyExistsException();
        }

        if (!authService.isEmailValid(mail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
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

    /**
     * Logs in an existing user with username and password.
     *
     * @param username The username of the user
     * @param password The password of the user
     * @return ResponseEntity containing access and refresh tokens upon successful login
     */
    @Operation(summary = "Login user",
            description = "Login an existing user with username and password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully logged in"),
                    @ApiResponse(responseCode = "401", description = "Wrong username or password") })
    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<?> loginUser(
            @Parameter(description = "Username", example = "john_doe") @RequestParam String username,
            @Parameter(description = "Password", example = "Password@123") @RequestParam String password) {

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

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param refreshToken The refresh token to be used for refreshing the access token
     * @return ResponseEntity containing a new access token
     */
    @Operation(summary = "Refresh token",
            description = "Refresh the access token using a valid refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully refreshed access token"),
                    @ApiResponse(responseCode = "401", description = "Invalid refresh token") })
    @PostMapping("/refreshToken")
    @CrossOrigin
    public ResponseEntity<?> refreshToken(@Parameter(description = "Refresh token", example = "your_refresh_token") @RequestParam String refreshToken) {
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

    /**
     * Validates the JWT access token.
     *
     * @return ResponseEntity indicating whether the token is valid or not
     */
    @Operation(summary = "Validate token",
            description = "Validate the JWT access token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token is valid"),
                    @ApiResponse(responseCode = "401", description = "Token is not valid") })
    @GetMapping("/validate-token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> validateToken() {
        // If this point is reached, the JWTAuthorizationFilter has already validated the token
        return ResponseEntity.ok().body("Token is valid");
    }
}
