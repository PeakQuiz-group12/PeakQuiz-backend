package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.QuestionDTO;
import idatt2105.peakquizbackend.dto.QuizCreateDTO;
import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.service.*;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between Quiz entities and DTOs.
 */
@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class QuizMapper {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    /**
     * INSTANCE variable for accessing the mapper instance.
     */
    public static final QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    /**
     * Converts a QuizCreateDTO to a Quiz entity.
     *
     * @param quizCreateDTO
     *            The QuizCreateDTO to convert
     * @return The corresponding Quiz entity
     */
    @Mapping(target = "playCount", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "games", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "collaborators", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategories")
    public abstract Quiz fromQuizCreateDTOtoEntity(QuizCreateDTO quizCreateDTO);

    /**
     * Updates a Quiz entity from a QuizResponseDTO.
     *
     * @param quizResponseDTO
     *            The QuizResponseDTO to update from
     * @param quiz
     *            The target Quiz entity to update
     */
    @Mapping(target = "games", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "collaborators", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "playCount", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract void updateQuizFromDTO(QuizResponseDTO quizResponseDTO, @MappingTarget Quiz quiz);

    /**
     * Retrieves the questions associated with a QuizResponseDTO.
     *
     * @param quizResponseDTO
     *            The QuizResponseDTO to retrieve questions from
     * @return The set of questions associated with the QuizResponseDTO
     */
    @Named("getQuestions")
    public Set<Question> getQuestionsCreate(QuizResponseDTO quizResponseDTO) {
        return quizResponseDTO.getQuestions().stream().map(question -> questionMapper.fromQuestionDTOtoEntity(question))
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves the questions associated with a QuizResponseDTO.
     *
     * @param responseDTO
     *            The QuizResponseDTO to retrieve questions from
     * @return A Page object containing the questions associated with the QuizResponseDTO
     */
    @Named("getQuestions")
    public Page<Question> getQuestions(QuizResponseDTO responseDTO) {
        return questionService.findQuestionsByQuizId(responseDTO.getId(), PageRequest.of(0, Integer.MAX_VALUE));
    }

    /**
     * Retrieves the categories associated with a QuizResponseDTO.
     *
     * @param responseDTO
     *            The QuizResponseDTO to retrieve categories from
     * @return The set of categories associated with the QuizResponseDTO
     */
    @Named("getCategories")
    public Set<Category> getCategories(QuizResponseDTO responseDTO) {
        return categoryService.findCategoriesByQuizId(responseDTO.getId());
    }

    /**
     * Maps category names to Category entities.
     *
     * @param categoryNames
     *            The set of category names to map
     * @return The set of corresponding Category entities
     */
    @Named("mapCategories")
    public Set<Category> mapCategories(Set<String> categoryNames) {
        return categoryNames.stream().map(name -> categoryService.findCategoryByName(name)).collect(Collectors.toSet());
    }

    /**
     * Maps Category entities to their names.
     *
     * @param categories
     *            The set of Category entities to map
     * @return The set of corresponding category names
     */
    @Named("mapCategoriesToNames")
    public Set<String> mapCategoriesToNames(Set<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toSet());
    }

    /**
     * Retrieves the IDs of the categories associated with a Quiz.
     *
     * @param quiz
     *            The Quiz to retrieve category IDs from
     * @return The set of category IDs associated with the Quiz
     */
    @Named("getCategoryIds")
    public Set<Long> getCategoryIds(Quiz quiz) {
        return quiz.getCategories().stream().map(Category::getId).collect(Collectors.toSet());
    }

    /**
     * Retrieves the IDs of the questions associated with a Quiz.
     *
     * @param quiz
     *            The Quiz to retrieve question IDs from
     * @return The set of question IDs associated with the Quiz
     */
    @Named("getQuestionIds")
    public Set<Long> questionIds(Quiz quiz) {
        return quiz.getQuestions().stream().map(Question::getId).collect(Collectors.toSet());
    }

    /**
     * Maps collaborators to their usernames.
     *
     * @param collaborators
     *            The set of Collaboration entities to map
     * @return The set of corresponding usernames
     */
    @Named("mapCollaborators")
    public Set<String> mapCollaborators(Set<Collaboration> collaborators) {
        return collaborators.stream().map(collaboration -> collaboration.getUser().getUsername())
                .collect(Collectors.toSet());
    }

    /**
     * Maps Question entities to QuestionDTOs.
     *
     * @param questions
     *            The set of Question entities to map
     * @return The set of corresponding QuestionDTOs
     */
    @Named("mapToQuestionDTOs")
    public Set<QuestionDTO> mapToQuestionDTOs(Set<Question> questions) {
        return questions.stream().map(question -> QuestionMapper.INSTANCE.toDTO(question)).collect(Collectors.toSet());
    }

    /**
     * Converts a Quiz entity to a QuizResponseDTO.
     *
     * @param quiz
     *            The Quiz entity to convert
     * @return The corresponding QuizResponseDTO
     */
    @Mapping(target = "collaboratorUsernames", source = "collaborators", qualifiedByName = "mapCollaborators")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategoriesToNames")
    @Mapping(target = "questions", source = "questions", qualifiedByName = "mapToQuestionDTOs")
    public abstract QuizResponseDTO toDTO(Quiz quiz);
}
