package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class SupportController {

    private final EmailService emailService;

    public SupportController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(summary = "Handle support query", description = "Send a support query via email")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Query sent successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request") })
    @PostMapping("/support")
    public ResponseEntity<String> handleSupportQuery(
            @Parameter(description = "Username of the user submitting the query", required = true, example = "john_doe") @RequestParam String username,
            @Parameter(description = "Subject of the query", required = true, example = "Issue with account activation") @RequestParam String subject,
            @Parameter(description = "Content of the query message", required = true, example = "I'm having trouble activating my account. Can you assist me?") @RequestParam String message) {
        emailService.sendEmail(username, subject, message);
        return ResponseEntity.ok("Your query has been sent. We will respond shortly.");
    }

    @Operation(summary = "Handle forgot password request", description = "Send a password reset email to the specified email address")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Password reset email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request") })
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> handleForgotPasswordRequest(
            @Parameter(description = "Email address of the user requesting password reset", required = true, example = "user@example.com") @RequestParam String email) {
        emailService.forgotPassword(email);
        return ResponseEntity.ok("Your password has been sent to your email.");
    }
}
