package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.QuestionDTO;
import idatt2105.peakquizbackend.mapper.QuestionMapper;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.service.QuestionService;
import idatt2105.peakquizbackend.service.QuizService;
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

  private final QuestionService questionService;
  private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

  private final QuizService quizService;

  //Quiz-iden får vi fra å lage en tom quiz
  @PostMapping
  public ResponseEntity<QuestionDTO> createQuestion(
      @RequestBody @NonNull QuestionDTO questionDTO) {
    Quiz quiz = quizService.findQuizById(questionDTO.getQuizId());

    LOGGER.info("Got request to create question: " + questionDTO);

    Question question = QuestionMapper.INSTANCE.fromQuestionDTOtoEntity(questionDTO);
    question.setQuiz(quiz);

    LOGGER.info("Saving question");

    Question savedQuestion = questionService.saveQuestion(question);

    LOGGER.info("Successfully saved question");

    return ResponseEntity.ok().body(QuestionMapper.INSTANCE.toDTO(savedQuestion));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> editQuestion(
      @PathVariable Long id,
      @RequestBody @NonNull QuestionDTO questionDTO)
  {
    LOGGER.info("Received put request for question with id: " + id + " - " + questionDTO);
    Question question = questionService.findQuestionById(id);

    LOGGER.info("Updating question");
    QuestionMapper.INSTANCE.updateQuestionFromDTO(questionDTO, question);

    LOGGER.info("Saving question");
    questionService.saveQuestion(question);

    LOGGER.info("Successfully updated question");

    return ResponseEntity.ok().body(questionDTO);
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
