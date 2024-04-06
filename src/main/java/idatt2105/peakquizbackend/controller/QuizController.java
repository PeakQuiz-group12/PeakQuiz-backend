package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.GameDTO;
import idatt2105.peakquizbackend.dto.QuestionDTO;
import idatt2105.peakquizbackend.dto.QuizCreateDTO;
import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.mapper.GameMapper;
import idatt2105.peakquizbackend.mapper.QuestionMapper;
import idatt2105.peakquizbackend.mapper.QuizMapper;
import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.service.CategoryService;
import idatt2105.peakquizbackend.service.QuestionService;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.SortingService;
import idatt2105.peakquizbackend.service.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final CategoryService categoryService;
  private final QuestionService questionService;

  @Autowired
  private final QuizMapper quizMapper;

  @Autowired
  private final QuestionMapper questionMapper;

  @Autowired
  private final GameMapper gameMapper;

  private final static Logger LOGGER = LoggerFactory.getLogger(QuizController.class);
  @Autowired
  private GameService gameService;

  @Operation(
      summary = "Get quizzes",
      description = "Gets all quizzes, allowing for pagination and sorting"
  )
  @GetMapping
  public ResponseEntity<Page<QuizResponseDTO>> getQuizzes(
      @Parameter(description = "Page number")
      @RequestParam(defaultValue = "0", required = false) int page,

      @Parameter(description = "Page size")
      @RequestParam(defaultValue = "6", required = false) int size,

      @Parameter(description = "Sorting column and ordering direction")
      @RequestParam(defaultValue = "id:asc", required = false) String[] sort
  ) {
    LOGGER.info("Received get-request for quizzes");
    System.out.println(Arrays.toString(sort));
    Sort sortCriteria = Sort.by(SortingService.convertToOrder(sort));
    Pageable pageable = PageRequest.of(page, size, sortCriteria);
    Page<Quiz> quizzes = quizService.findAllQuizzes(pageable);

    Page<QuizResponseDTO> quizResponseDTOS = quizzes.map(quizMapper::toDTO);
    LOGGER.info("Successfully found quizzes");

    return ResponseEntity.ok(quizResponseDTOS);
  }

  @Operation(
          summary = "Get quiz",
          description = "Get quiz based on its id"
  )
  @GetMapping("/{id}")
  public ResponseEntity<Quiz> getQuiz(
           @PathVariable Long id
  ) {
    LOGGER.info("Received request for quiz with id: " + id);
    Quiz quiz = quizService.findQuizById(id);

    LOGGER.info("Successfully returned quiz");
    return ResponseEntity.ok(quiz);
  }

    @Operation(
          summary = "Get quiz",
          description = "Get quiz based on its id"
  )
  @PostMapping
  public ResponseEntity<QuizResponseDTO> createQuiz(
      @RequestBody @NonNull QuizCreateDTO quizCreateDTO
  )
  {
    LOGGER.info("Received post request for quiz: " + quizCreateDTO);
    Quiz quiz = quizMapper.fromQuizCreateDTOtoEntity(quizCreateDTO);
    quiz.getCategories().forEach(c -> c.addQuiz(quiz));
    QuizResponseDTO quizResponseDTO = quizMapper.toDTO(quizService.saveQuiz(quiz));
    System.out.println(quiz);

    LOGGER.info("Successfully saved quiz");
    return ResponseEntity.ok(quizResponseDTO);
  }


  @PutMapping("/{id}")
  public ResponseEntity<QuizResponseDTO> editQuiz(
      @PathVariable Long id,
      @RequestBody QuizResponseDTO quizResponseDTO
  )
  {
    LOGGER.info("Received put request for quiz: " + quizResponseDTO);
    Quiz quiz = quizService.findQuizById(id);
    QuizMapper.INSTANCE.updateQuizFromDTO(quizResponseDTO, quiz);

    updateQuizCategories(quiz, quizResponseDTO.getCategories());

    updateQuestions(quiz, quizResponseDTO.getQuestions());

    System.out.println(quiz.getQuestions());

    Quiz newQuiz = (quizService.saveQuiz(quiz));
    System.out.println(newQuiz);

    LOGGER.info("Successfully updated quiz");
    return ResponseEntity.ok(quizResponseDTO);
  }

  @GetMapping("/games/{id}")
  public ResponseEntity<List<GameDTO>> getGames(
          @PathVariable Long id
  )
  {
    LOGGER.info("Received request fo games by quiz with id: " + id);
    List<Game> games = gameService.findGamesByQuizId(id);

    LOGGER.info("Successfully found games");

    List<GameDTO> gameDTOs = gameMapper.toDTOs(games);


    return ResponseEntity.ok(gameDTOs);
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQuiz(
      @PathVariable Long id) {
    LOGGER.info("Received delete request for quiz_id: " + id);

    quizService.deleteQuizById(id);

    return ResponseEntity.noContent().build();
  }

  private void updateQuizCategories(Quiz quiz, Set<String> categoryNames) {

    if (categoryNames == null)
      return;

    // Fetch the existing list of categories associated with the quiz
    Set<String> existingNames = quiz.getCategories().stream()
        .map(Category::getName)
        .collect(Collectors.toSet());

    // Remove categories from the join table that are present in the existing list but not in the updated list
    existingNames.stream()
        .filter(name -> !categoryNames.contains(name))
        .forEach(name -> {
          Category category = categoryService.findCategoryByName(name);
          categoryService.findCategoriesByQuizId(quiz.getId());
          category.removeQuiz(quiz);
        });

    // Add new categories from the updated list that are not present in the existing list to the join table
    categoryNames.stream()
        .filter(name -> !existingNames.contains(name))
        .forEach(name -> {
          Category category = categoryService.findCategoryByName(name);
          category.addQuiz(quiz);
        });
  }
  private void updateQuestions(Quiz quiz, Set<QuestionDTO> questionDTOs) {
    if (questionDTOs == null) return;

    Set<Question> questions = questionDTOs.stream().map(questionMapper::fromQuestionDTOtoEntity).collect(
        Collectors.toSet());
    Set<Question> existingQuestions = quiz.getQuestions();
    existingQuestions.stream()
        .filter(question -> !questions.contains(question))
        .forEach(question -> {
          quiz.removeQuestion(question);
          questionService.deleteQuestion(question.getId());
        });

    questions.stream()
        .filter(question-> !existingQuestions.contains(question))
        .forEach(question -> {
          questionService.saveQuestion(question);
          quiz.addQuestion(question);
        });
  }
}
