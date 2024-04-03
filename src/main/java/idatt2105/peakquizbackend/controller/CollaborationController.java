package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import idatt2105.peakquizbackend.service.CollaborationService;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.SortingService;
import idatt2105.peakquizbackend.service.UserService;
import java.util.Optional;
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

  @PostMapping("/")
  public ResponseEntity<?> createCollaboration(
      @RequestParam Long userId,
      @RequestParam Long quizId,
      @RequestParam CollaboratorType collaborationType
  ) {
    Optional<User> _user = userService.findUserByUserId(userId);
    Optional<Quiz> _quiz = quizService.findQuizById(quizId);

    if (_user.isEmpty() || _quiz.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    // TODO: convert to DTO if possible
    Collaboration collaboration = collaborationService.saveCollaboration(_user.get(), _quiz.get(), collaborationType);
    return ResponseEntity.ok(collaboration);
  }

  // TODO: Change
  @GetMapping("/user")
  public ResponseEntity<?> getCollaborators(
      @RequestParam Long quizId,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "5", required = false) int size,
      @RequestParam(defaultValue = "username,asc", required = false) String[] sort
  )
  {
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

  @GetMapping("/quiz")
  public ResponseEntity<?> getUserQuizzes(
      @RequestParam Long userId,
      @RequestParam CollaboratorType collaboratorType,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "5", required = false) int size,
      @RequestParam(defaultValue = "createdOn,desc") String[] sort
  ) {
    LOGGER.info("Received get request for quizzes of: " + userId + " with type: " + collaboratorType.toString());

    Pageable pageable = PageRequest.of(page, size, Sort.by(SortingService.convertToOrder(sort)));
    Page<QuizResponseDTO> quizzes = collaborationService.findQuizzesByUserId(userId, collaboratorType, pageable);

    LOGGER.info("Successfully retrieved quizzes");
    return ResponseEntity.ok(quizzes);
  }
}
