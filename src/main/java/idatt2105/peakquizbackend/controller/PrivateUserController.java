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
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import idatt2105.peakquizbackend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@CrossOrigin
public class PrivateUserController {

    private final QuizService quizService;
    private final CollaborationService collaborationService;
    private final UserService userService;
    private final AuthService authService;

    private final Logger LOGGER = LoggerFactory.getLogger(PrivateUserController.class);

    /**
     * Retrieves information about the currently authenticated user.
     *
     * @param authentication
     *            Authentication object representing the current user
     * @return ResponseEntity containing user information
     */
    @Operation(summary = "Get current user", description = "Retrieve information about the currently authenticated user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved current user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }) })
    @GetMapping
    public ResponseEntity<UserDTO> getMe(
            @Parameter(description = "Authentication object representing the current user") Authentication authentication) {
        return ResponseEntity.ok(UserMapper.INSTANCE.toDTO(userService.findUserByUsername(authentication.getName())));
    }

    /**
     * Updates the password of the currently authenticated user.
     *
     * @param authentication
     *            Authentication object representing the current user
     * @param password
     *            UserUpdateDTO containing the new password
     * @return ResponseEntity indicating the result of the password update
     */
    @Operation(summary = "Update current user", description = "Update the password of the currently authenticated user", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated password", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid password format") })
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

    /**
     * Creates a new collaboration between the current user and a quiz.
     *
     * @param authentication
     *            Authentication object representing the current user
     * @param collaborationDTO
     *            CollaborationDTO containing information about the collaboration
     * @return ResponseEntity containing the created collaboration
     */
    @Operation(summary = "Create collaboration", description = "Create a new collaboration between a user and a quiz", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully created collaboration", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CollaborationDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Quiz not found") })
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

    /**
     * Retrieves quizzes of the current user with a specified collaborator type.
     *
     * @param authentication
     *            Authentication object representing the current user
     * @param collaboratorType
     *            Collaborator type
     * @param page
     *            Page number
     * @param size
     *            Page size
     * @param sort
     *            Sorting criteria
     * @return ResponseEntity containing the page of quizzes
     */
    @Operation(summary = "Get users quizzes", description = "Get quizzes of a user with specified collaborator type", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's quizzes", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = QuizResponseDTO.class)) }) })
    @GetMapping("/quizzes")
    public ResponseEntity<Page<QuizResponseDTO>> getUserQuizzes(
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
