package idatt2105.peakquizbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Checks if a password meets the strength criteria.
     * @param password The password to check
     * @return true if the password meets the criteria, false otherwise
     */
    public boolean isPasswordStrong(String password) {
        // Example criteria: at least 8 characters, including numbers, letters and at least one special character
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }

    public boolean isEmailValid(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

    /**
     * Encrypts a password using BCrypt.
     * @param password The password to encrypt
     * @return The encrypted password
     */
    public String encryptPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * Checks if an input password matches the stored (encrypted) password.
     * @param inputPassword The input password to check
     * @param storedPassword The stored (encrypted) password to compare with
     * @return true if the input password matches the stored password, false otherwise
     */
    public boolean matches(String inputPassword, String storedPassword) {
        return bCryptPasswordEncoder.matches(inputPassword, storedPassword);
    }

    /**
     * Generates a JWT token for authentication.
     * @param userId The user ID to include in the token
     * @param validMinutes The duration of token validity in minutes
     * @param secret The secret key used to sign the token
     * @return The generated JWT token
     */
    public String generateToken(final String userId, final Duration validMinutes, String secret) {
        final Instant now = Instant.now();
        final Algorithm hmac512 = Algorithm.HMAC512(secret);
        final JWTVerifier verifier = JWT.require(hmac512).build();
        return JWT.create().withSubject(userId).withIssuer("idatt2105_token_issuer_app").withIssuedAt(now)
                .withExpiresAt(now.plusMillis(validMinutes.toMillis())).sign(hmac512);
    }
}
