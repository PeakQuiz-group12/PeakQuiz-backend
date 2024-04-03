package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.QuestionCreateDTO;
import idatt2105.peakquizbackend.dto.QuestionResponseDTO;
import idatt2105.peakquizbackend.mappers.QuestionMapper;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.service.QuestionService;
import idatt2105.peakquizbackend.service.QuizService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/questions")
@AllArgsConstructor
public class QuestionController {

  private final QuestionMapper questionMapper;
  private final QuestionService questionService;
  private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

  private final QuizService quizService;

  //Quiz-iden får vi fra å lage en tom quiz
  @PostMapping("/")
  public ResponseEntity<?> createQuestion(
      @RequestBody @NonNull QuestionCreateDTO questionCreateDTO) {
    Optional<Quiz> quiz = quizService.findQuizById(questionCreateDTO.getQuizId());

    LOGGER.info("Got request to create question: " + questionCreateDTO);

    if (quiz.isEmpty()) {

      LOGGER.error("Could not find quiz with id: " + questionCreateDTO.getQuizId());

      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "Could not find parent quiz");
      return ResponseEntity.badRequest().body(errorResponse);
    }
    Question question = questionMapper.fromQuestionCreateDTOtoEntity(questionCreateDTO);
    question.setQuiz(quiz.get());

    LOGGER.info("Saving question");

    Question savedQuestion = questionService.saveQuestion(question);

    LOGGER.info("Successfully saved question");

    return ResponseEntity.ok().body(questionMapper.toDTO(savedQuestion));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> editQuestion(
      @PathVariable Long id,
      @RequestBody @NonNull QuestionResponseDTO questionResponseDTO)
  {
    LOGGER.info("Received put request for question with id: " + id + " - " + questionResponseDTO);
    Optional<Question> _question = questionService.findQuestionById(id);

    if (_question.isEmpty()) {
      LOGGER.error("Could not find question");
      return ResponseEntity.notFound().build();
    }

    Question question = _question.get();
    LOGGER.info("Updating question");
    questionMapper.updateQuestionFromDTO(questionResponseDTO, question);

    LOGGER.info("Saving question");
    questionService.saveQuestion(question);

    LOGGER.info("Successfully updated question");

    return ResponseEntity.ok().body(questionResponseDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteQuestion(
      @PathVariable Long id
  )
  {
    LOGGER.info("Received delete request for question_id: " + id);
    questionService.deleteQuestion(id);
    return ResponseEntity.noContent().build();
  }
}
