package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    /**
     * Gets all quizzes, allowing for pagination and sorting.
     *
     * @param page
     *            Page number
     * @param size
     *            Page size
     * @param sort
     *            Sorting column and ordering direction
     * @param isTemplate
     *            Boolean that decides whether to filter by templates
     * @param difficulty
     *            Quiz difficulty
     * @param categoryNames
     *            Categories
     * @return ResponseEntity containing the page of quizzes
     */
    @Operation(summary = "Get quizzes", description = "Gets all quizzes, allowing for pagination and sorting")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved quizzes") })
    @GetMapping
    public ResponseEntity<Page<QuizResponseDTO>> getQuizzes(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0", required = false) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "6", required = false) int size,
            @Parameter(description = "Sorting column and ordering direction") @RequestParam(defaultValue = "id:asc", required = false) String[] sort,
            @Parameter(description = "Boolean that decides whether to filter by templates") @RequestParam(defaultValue = "false", required = false) boolean isTemplate,
            @Parameter(description = "Quiz difficulty") @RequestParam(required = false) Byte difficulty,
            @Parameter(description = "Categories") @RequestParam(required = false) List<String> categoryNames) {
        LOGGER.info("Received get-request for quizzes");

        Sort sortCriteria = Sort.by(SortingService.convertToOrder(sort));
        Pageable pageable = PageRequest.of(page, size, sortCriteria);

        Page<Quiz> quizzes;
        if (isTemplate) {
            quizzes = quizService.findAllTemplates(pageable);
        } else if (categoryNames != null) {
            List<Long> categoryIds = categoryNames.stream()
                    .map(name -> categoryService.findCategoryByName(name).getId()).toList();
            quizzes = quizService.findQuizzesWithFilters(categoryIds, pageable);
        } else {
            quizzes = quizService.findAllQuizzes(pageable);
        }

        Page<QuizResponseDTO> quizResponseDTOS = quizzes.map(quizMapper::toDTO);
        LOGGER.info("Successfully found quizzes");

        return ResponseEntity.ok(quizResponseDTOS);
    }

    /**
     * Get quiz based on its id.
     *
     * @param id
     *            ID of the quiz to be retrieved
     * @return ResponseEntity containing the quiz
     */
    @Operation(summary = "Get quiz", description = "Get quiz based on its id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved quiz"),
            @ApiResponse(responseCode = "404", description = "Quiz not found") })
    @GetMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> getQuiz(
            @Parameter(description = "ID of the quiz to be retrieved", required = true) @PathVariable Long id) {
        LOGGER.info("Received request for quiz with id: {}", id);
        Quiz quiz = quizService.findQuizById(id);

        LOGGER.info("Successfully returned quiz");
        return ResponseEntity.ok(quizMapper.toDTO(quiz));
    }

    /**
     * Create a new quiz.
     *
     * @param quizCreateDTO
     *            Quiz data to create
     * @return ResponseEntity containing the created quiz
     */
    @Operation(summary = "Create quiz", description = "Create a new quiz")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully created quiz") })
    @PostMapping
    public ResponseEntity<QuizResponseDTO> createQuiz(
            @Parameter(description = "Quiz data to create", required = true) @RequestBody @

            NonNull QuizCreateDTO quizCreateDTO) {
        LOGGER.info("Received post request for quiz: {}", quizCreateDTO);
        Quiz quiz = quizMapper.fromQuizCreateDTOtoEntity(quizCreateDTO);
        quiz.getCategories().forEach(c -> c.addQuiz(quiz));
        QuizResponseDTO quizResponseDTO = quizMapper.toDTO(quizService.saveQuiz(quiz));
        System.out.println(quiz);

        LOGGER.info("Successfully saved quiz");
        return ResponseEntity.ok(quizResponseDTO);
    }

    /**
     * Edit an existing quiz.
     *
     * @param id
     *            ID of the quiz to be edited
     * @param quizResponseDTO
     *            Updated quiz data
     * @return ResponseEntity containing the updated quiz
     */
    @Operation(summary = "Edit quiz", description = "Edit an existing quiz")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully updated quiz"),
            @ApiResponse(responseCode = "404", description = "Quiz not found") })
    @PutMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> editQuiz(
            @Parameter(description = "ID of the quiz to be edited", required = true) @PathVariable Long id,
            @Parameter(description = "Updated quiz data", required = true) @RequestBody QuizResponseDTO quizResponseDTO) {
        LOGGER.info("Received put request for quiz: " + quizResponseDTO);
        Quiz quiz = quizService.findQuizById(id);
        QuizMapper.INSTANCE.updateQuizFromDTO(quizResponseDTO, quiz);

        // Does nothing to categories and questions if you don't send it
        System.out.println("updating categoreis");
        updateQuizCategories(quiz, quizResponseDTO.getCategories());

        System.out.println("updating updateing question");
        updateQuestions(quiz, quizResponseDTO.getQuestions());

        System.out.println("Done");
        System.out.println(quiz.getQuestions());

        Quiz newQuiz = (quizService.saveQuiz(quiz));
        System.out.println(newQuiz);

        LOGGER.info("Successfully updated quiz");
        return ResponseEntity.ok(QuizMapper.INSTANCE.toDTO(quiz));
    }

    /**
     * Get all games associated with a quiz by its ID.
     *
     * @param id
     *            ID of the quiz to retrieve games for
     * @return ResponseEntity containing the set of games
     */
    @Operation(summary = "Get games by quiz ID", description = "Get all games associated with a quiz by its ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved games"),
            @ApiResponse(responseCode = "404", description = "Quiz not found") })
    @GetMapping("/games/{id}")
    public ResponseEntity<Set<GameDTO>> getGames(
            @Parameter(description = "ID of the quiz to retrieve games for", required = true) @PathVariable Long id) {
        LOGGER.info("Received request for games by quiz with id: {}", id);
        Set<Game> games = quizService.findQuizById(id).getGames();

        LOGGER.info("Successfully found games");

        Set<GameDTO> gameDTOs = gameMapper.toDTOs(games);

        return ResponseEntity.ok(gameDTOs);
    }

    /**
     * Delete a quiz by its ID.
     *
     * @param id
     *            ID of the quiz to be deleted
     * @return ResponseEntity indicating the result of the deletion
     */
    @Operation(summary = "Delete quiz", description = "Delete a quiz by its ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Successfully deleted quiz"),
            @ApiResponse(responseCode = "404", description = "Quiz not found") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(
            @Parameter(description = "ID of the quiz to be deleted", required = true) @PathVariable Long id) {
        LOGGER.info("Received delete request for quiz_id: {}", id);
        quizService.deleteQuizById(id);

        return ResponseEntity.noContent().build();
    }

    private void updateQuizCategories(Quiz quiz, Set<String> categoryNames) {

        if (categoryNames == null)
            return;

        // Fetch the existing list of categories associated with the quiz
        Set<String> existingNames = quiz.getCategories().stream().map(Category::getName).collect(Collectors.toSet());

        // Remove categories from the join table that are present in the existing list but not in the updated list
        existingNames.stream().filter(name -> !categoryNames.contains(name)).forEach(name -> {
            Category category = categoryService.findCategoryByName(name);
            categoryService.findCategoriesByQuizId(quiz.getId());
            category.removeQuiz(quiz);
        });

        // Add new categories from the updated list that are not present in the existing list to the join table
        categoryNames.stream().filter(name -> !existingNames.contains(name)).forEach(name -> {
            Category category = categoryService.findCategoryByName(name);
            category.addQuiz(quiz);
        });
    }

    private void updateQuestions(Quiz quiz, Set<QuestionDTO> questionDTOs) {
        if (questionDTOs == null)
            return;

        Set<Question> questions = questionDTOs.stream().map(questionMapper::fromQuestionDTOtoEntity)
                .collect(Collectors.toSet());
        Set<Question> existingQuestions = quiz.getQuestions();
        List<Question> omittedQuestions = new ArrayList<>();
        existingQuestions.stream().filter(question -> !questions.contains(question)).forEach(question -> {
            omittedQuestions.add(question);
            questionService.deleteQuestion(question.getId());
        });

        omittedQuestions.forEach(quiz.getQuestions()::remove);

        questions.stream().filter(question -> !existingQuestions.contains(question)).forEach(question -> {
            questionService.saveQuestion(question);
            quiz.addQuestion(question);
        });
    }

    /*
     * 
     * @Operation(summary = "Get collaborators", description = "Get collaborators of a quiz")
     * 
     * @GetMapping("/user") public ResponseEntity<?> getCollaborators(
     * 
     * @Parameter(description = "Quiz ID") @RequestParam Long quizId,
     * 
     * @Parameter(description = "Page number") @RequestParam(defaultValue = "0", required = false) int page,
     * 
     * @Parameter(description = "Page size") @RequestParam(defaultValue = "5", required = false) int size,
     * 
     * @Parameter(description = "Sorting criteria") @RequestParam(defaultValue = "username:asc", required = false)
     * String[] sort) { LOGGER.info("Received get request for collaborators of quiz: " + quizId);
     * 
     * Pageable pageable = PageRequest.of(page, size, Sort.by(SortingService.convertToOrder(sort))); Page<UserDTO>
     * collaborators = collaborationService.findCollaboratorsByQuizId(quizId, pageable);
     * 
     * if (collaborators.isEmpty()) { LOGGER.error("Could not find quiz with id: " + quizId); return
     * ResponseEntity.notFound().build(); }
     * 
     * LOGGER.info("Successfully returned collaborators."); return ResponseEntity.ok(collaborators); }
     */
}
