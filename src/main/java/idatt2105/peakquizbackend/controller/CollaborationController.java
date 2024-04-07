package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.CollaborationDTO;
import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.mapper.CollaborationMapper;
import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import idatt2105.peakquizbackend.service.CollaborationService;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.SortingService;
import idatt2105.peakquizbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/collaborations")
public class CollaborationController {

    private final CollaborationService collaborationService;
    private final UserService userService;
    private final QuizService quizService;
    private final static Logger LOGGER = LoggerFactory.getLogger(QuizController.class);

    public CollaborationController(CollaborationService collaborationService, UserService userService,
            QuizService quizService) {
        this.collaborationService = collaborationService;
        this.userService = userService;
        this.quizService = quizService;
    }

    @Operation(summary = "Create collaboration", description = "Create a new collaboration between a user and a quiz")
    @PostMapping
    public ResponseEntity<CollaborationDTO> createCollaboration(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Quiz ID") @RequestParam Long quizId,
            @Parameter(description = "Collaborator type") @RequestParam CollaboratorType collaborationType) {
        User user = userService.findUserByUserId(userId);
        Quiz quiz = quizService.findQuizById(quizId);

        Collaboration collaboration = collaborationService.saveCollaboration(user, quiz, collaborationType);
        CollaborationDTO dto = CollaborationMapper.INSTANCE.toDTO(collaboration);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get collaborators", description = "Get collaborators of a quiz")
    @GetMapping("/user")
    public ResponseEntity<?> getCollaborators(@Parameter(description = "Quiz ID") @RequestParam Long quizId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0", required = false) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "5", required = false) int size,
            @Parameter(description = "Sorting criteria") @RequestParam(defaultValue = "username:asc", required = false) String[] sort) {
        LOGGER.info("Received get request for collaborators of quiz: " + quizId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(SortingService.convertToOrder(sort)));
        Page<UserDTO> collaborators = collaborationService.findCollaboratorsByQuizId(quizId, pageable);

        if (collaborators.isEmpty()) {
            LOGGER.error("Could not find quiz with id: " + quizId);
            return ResponseEntity.notFound().build();
        }

        LOGGER.info("Successfully returned collaborators.");
        return ResponseEntity.ok(collaborators);
    }

    @Operation(summary = "Get user quizzes", description = "Get quizzes of a user with specified collaborator type")
    @GetMapping("/quiz")
    public ResponseEntity<?> getUserQuizzes(@Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Collaborator type") @RequestParam CollaboratorType collaboratorType,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0", required = false) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "5", required = false) int size,
            @Parameter(description = "Sorting criteria") @RequestParam(defaultValue = "createdOn:desc") String[] sort) {
        LOGGER.info("Received get request for quizzes of: " + userId + " with type: " + collaboratorType.toString());

        Pageable pageable = PageRequest.of(page, size, Sort.by(SortingService.convertToOrder(sort)));
        Page<QuizResponseDTO> quizzes = collaborationService.findQuizzesByUserId(userId, collaboratorType, pageable);

        LOGGER.info("Successfully retrieved quizzes");
        return ResponseEntity.ok(quizzes);
    }
}
