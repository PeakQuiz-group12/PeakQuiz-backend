package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SupportController {

    private final EmailService emailService;

    public SupportController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(summary = "Handle support query", description = "Send a support query via email")
    @PostMapping("/support")
    public ResponseEntity<?> handleSupportQuery(
            @Parameter(description = "Username of the user submitting the query", required = true) @RequestParam String username,
            @Parameter(description = "Subject of the query", required = true) @RequestParam String subject,
            @Parameter(description = "Content of the query message", required = true) @RequestParam String message) {
        emailService.sendEmail(username, subject, message);
        return ResponseEntity.ok("Your query has been sent. We will respond shortly.");
    }

    @Operation(summary = "Handle forgot password request", description = "Send a password reset email to the specified email address")
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> handleSupportQuery(
            @Parameter(description = "Email address of the user requesting password reset", required = true) @RequestParam String email) {
        emailService.forgotPassword(email);
        return ResponseEntity.ok("Your password has been sent to your email.");
    }
}
