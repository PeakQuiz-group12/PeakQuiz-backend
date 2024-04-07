package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SupportController {

    private final EmailService emailService;

    public SupportController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/support")
    @CrossOrigin
    public ResponseEntity<?> handleSupportQuery(@RequestParam String username, @RequestParam String subject,
            @RequestParam String message) {
        emailService.sendEmail(username, subject, message);
        return ResponseEntity.ok("Your query has been sent. We will respond shortly.");
    }

    @PostMapping("/forgotPassword")
    @CrossOrigin
    public ResponseEntity<?> handleSupportQuery(@RequestParam String email) {
        emailService.forgotPassword(email);
        return ResponseEntity.ok("Your password has been sent to you email");
    }
}
