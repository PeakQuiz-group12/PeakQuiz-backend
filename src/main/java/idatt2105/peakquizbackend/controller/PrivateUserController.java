package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.CollaborationDTO;
import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.dto.UserUpdateDTO;
import idatt2105.peakquizbackend.exceptions.BadInputException;
import idatt2105.peakquizbackend.mapper.CollaborationMapper;
import idatt2105.peakquizbackend.mapper.UserMapper;
import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.AuthService;
import idatt2105.peakquizbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import idatt2105.peakquizbackend.service.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me")
@AllArgsConstructor
public class PrivateUserController {

    private final QuizService quizService;
    private final CollaborationService collaborationService;
    private final UserService userService;
    private final AuthService authService;

    private final Logger LOGGER = LoggerFactory.getLogger(PrivateUserController.class);

    @Operation(summary = "Get current user", description = "Retrieve information about the currently authenticated user")
    @GetMapping
    public ResponseEntity<UserDTO> getMe(
            @Parameter(description = "Authentication object representing the current user") Authentication authentication) {
        return ResponseEntity.ok(UserMapper.INSTANCE.toDTO(userService.findUserByUsername(authentication.getName())));
    }

    @Operation(summary = "Update current user", description = "Update the password of the currently authenticated user")
    @PutMapping
    public ResponseEntity<String> updateMe(
            @Parameter(description = "Authentication object representing the current user") Authentication authentication,
            @RequestBody UserUpdateDTO password) {
        User user = userService.findUserByUsername(authentication.getName());

        if (password != null) {
            if (!authService.isPasswordStrong(password.getPassword())) {
                throw new BadInputException(
                        "Password must be at least 8 characters long, include numbers, upper and lower case letters, and at least one special character");
            }
            user.setPassword(authService.encryptPassword(password.getPassword()));
            userService.saveUser(user);
            String message = "Updated password";
            return ResponseEntity.ok(message);

        } else {
            throw new BadInputException(
                    "Password must be at least 8 characters long, include numbers, upper and lower case letters, and at least one special character");
        }
    }

    @Operation(summary = "Create collaboration", description = "Create a new collaboration between a user and a quiz")
    @PostMapping("/collaborations")
    public ResponseEntity<CollaborationDTO> createCollaboration(
            @Parameter(description = "Authentication object representing the current user") Authentication authentication,
            @Parameter(description = "Collaboration DTO") @RequestBody CollaborationDTO collaborationDTO) {
        Quiz quiz = quizService.findQuizById(collaborationDTO.getQuizId());
        User user = userService.findUserByUsername(authentication.getName());

        Collaboration collaboration = collaborationService.saveCollaboration(user, quiz,
                collaborationDTO.getCollaboratorType());
        CollaborationDTO dto = CollaborationMapper.INSTANCE.toDTO(collaboration);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get users quizzes", description = "Get quizzes of a user with specified collaborator type")
    @GetMapping("/quizzes")
    public ResponseEntity<?> getUserQuizzes(
            @Parameter(description = "Authentication object representing the current user") Authentication authentication,
            @Parameter(description = "Collaborator type") @RequestParam CollaboratorType collaboratorType,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0", required = false) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "5", required = false) int size,
            @Parameter(description = "Sorting criteria") @RequestParam(defaultValue = "joinedOn:desc", required = false) String[] sort) {
        LOGGER.info("Received get request for quizzes of: {} with type: {}", authentication.getName(),
                collaboratorType.toString());

        User user = userService.findUserByUsername(authentication.getName());

        Pageable pageable = PageRequest.of(page, size, Sort.by(SortingService.convertToOrder(sort)));
        Page<QuizResponseDTO> quizzes = collaborationService.findQuizzesByUserId(user.getId(), collaboratorType,
                pageable);

        LOGGER.info("Successfully retrieved quizzes");
        return ResponseEntity.ok(quizzes);
    }
}
