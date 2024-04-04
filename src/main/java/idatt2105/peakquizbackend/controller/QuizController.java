package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.QuizCreateDTO;
import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.mapper.QuizMapper;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.service.CollaborationService;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.SortingService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/quizzes")
public class QuizController {

  private final QuizService quizService;
  private final CollaborationService collaborationService;

  private final static Logger LOGGER = LoggerFactory.getLogger(QuizController.class);

  @GetMapping("/")
  public ResponseEntity<Page<Quiz>> getQuizzes(
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "6", required = false) int size,
      @RequestParam(defaultValue = "id,asc", required = false) String[] sort
  ) {
    LOGGER.info("Received get-request for quizzes");
    Sort sortCriteria = Sort.by(SortingService.convertToOrder(sort));
    Pageable pageable = PageRequest.of(page, size, sortCriteria);
    Page<Quiz> quizzes = quizService.findAllQuizzes(pageable);
    LOGGER.info("Successfully found quizzes");

    return ResponseEntity.ok(quizzes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Quiz> getQuiz(
           @PathVariable Long id
  ) {
    LOGGER.info("Received request for quiz with id: " + id);
    Quiz quiz = quizService.findQuizById(id);

    LOGGER.info("Successfully returned quiz");
    return ResponseEntity.ok(quiz);
  }

  @PostMapping("/")
  public ResponseEntity<QuizResponseDTO> createQuiz(
      @RequestBody @NonNull QuizCreateDTO quizCreateDTO
  )
  {
    LOGGER.info("Received post request for quiz: " + quizCreateDTO);
    Quiz quiz = QuizMapper.INSTANCE.fromQuizCreateDTOtoEntity(quizCreateDTO);
    QuizResponseDTO quizResponseDTO = QuizMapper.INSTANCE.toDTO(quizService.saveQuiz(quiz));

    LOGGER.info("Successfully saved quiz");
    return ResponseEntity.ok(quizResponseDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<QuizResponseDTO> editQuiz(
      @PathVariable Long id,
      @RequestBody QuizResponseDTO quizResponseDTO
  )
  {
    LOGGER.info("Received put request for quiz with id: " + id);
    Quiz quiz = quizService.findQuizById(id);
    quizService.saveQuiz(quiz);
    QuizMapper.INSTANCE.updateQuizFromDTO(quizResponseDTO, quiz);
    LOGGER.info("Successfully updated quiz");
    return ResponseEntity.ok(quizResponseDTO);
  }



  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQuiz(
      @PathVariable Long id) {
    LOGGER.info("Received delete request for quiz_id: " + id);

    quizService.deleteQuizById(id);

    return ResponseEntity.noContent().build();
  }
}
