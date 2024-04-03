package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.QuestionDTO;
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
  @PostMapping("/create")
  public ResponseEntity<?> createQuestion(
      @RequestBody @NonNull QuestionDTO questionDTO) {
    Optional<Quiz> quiz = quizService.findQuizById(questionDTO.getQuizId());

    LOGGER.info("Got request to create question: " + questionDTO);

    if (quiz.isEmpty()) {
      LOGGER.error("Could not find quiz with id: " + questionDTO.getQuizId());
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "Could not find parent quiz");
      return ResponseEntity.badRequest().body(errorResponse);
    }
    Question question = questionMapper.toEntity(questionDTO);
    question.setQuiz(quiz.get());
    questionService.saveQuestion(question);
    questionDTO.setId(question.getId());
    return ResponseEntity.ok().body(questionDTO);
  }

  @PutMapping("edit/{id}")
  public ResponseEntity<?> editQuestion(
      @PathVariable Long id,
      @RequestBody @NonNull QuestionDTO questionDTO) {
    Optional<Question> _question = questionService.findQuestionById(id);

    if (_question.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Question question = _question.get();
    questionMapper.updateQuestionFromDTO(questionDTO, question);
    questionService.saveQuestion(question);
    return ResponseEntity.ok().body(questionDTO);
  }

  @DeleteMapping("delete/{id}")
  public ResponseEntity<?> deleteQuestion(
      @PathVariable Long id
  )
  {
    questionService.deleteQuestion(id);
    return ResponseEntity.noContent().build();
  }
}
