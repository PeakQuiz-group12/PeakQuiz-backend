package idatt2105.peakquizbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
public class AuthenticationController {

  @GetMapping("/validate-token")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> validateToken() {
    // If this point is reached, the JWTAuthorizationFilter has already validated the token
    return ResponseEntity.ok().body("Token is valid");
  }
}
