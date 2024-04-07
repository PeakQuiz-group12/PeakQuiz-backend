package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.QuestionDTO;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.service.QuizService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper interface for converting between Question entities and QuestionDTOs.
 */
@Mapper(componentModel = "spring")
public abstract class QuestionMapper {

    @Autowired
    private QuizService quizService;

    /**
     * INSTANCE variable for accessing the mapper instance.
     */
    public static final QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    /**
     * Converts a QuestionDTO to a Question entity.
     *
     * @param questionDTO The QuestionDTO to convert
     * @return The corresponding Question entity
     */
    @Mapping(target = "quiz", source = "quizId", qualifiedByName = "mapQuiz")
    public abstract Question fromQuestionDTOtoEntity(QuestionDTO questionDTO);

    /**
     * Maps the quizId to a Quiz entity.
     *
     * @param quizId The quizId to map
     * @return The corresponding Quiz entity
     */
    @Named("mapQuiz")
    Quiz mapQuiz(Long quizId) {
        return quizService.findQuizById(quizId);
    }

    /**
     * Converts a QuestionDTO to a Question entity for updating.
     *
     * @param questionDTO The QuestionDTO to convert
     */
    @Mapping(target = "quiz", ignore = true)
    public abstract Question fromQuestionResponseDTOtoEntity(QuestionDTO questionDTO);

    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract void updateQuestionFromDTO(QuestionDTO questionDTO, @MappingTarget Question question);

    /**
     * Converts a Question entity to a QuestionDTO.
     *
     * @param question The Question entity to convert
     * @return The corresponding QuestionDTO
     */
    @Mapping(target = "quizId", source = "quiz", qualifiedByName = "mapToQuizId")
    public abstract QuestionDTO toDTO(Question question);

    /**
     * Maps the Quiz entity to its ID.
     *
     * @param quiz The Quiz entity to map
     * @return The ID of the Quiz entity
     */
    @Named("mapToQuizId")
    public Long mapToQuizId(Quiz quiz) {
        return quiz.getId();
    }
}