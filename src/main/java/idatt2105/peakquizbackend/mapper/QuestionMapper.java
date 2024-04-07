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

@Mapper(componentModel = "spring")
public abstract class QuestionMapper {

    @Autowired
    private QuizService quizService;

    public static QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mapping(target = "quiz", source = "quizId", qualifiedByName = "mapQuiz")
    public abstract Question fromQuestionDTOtoEntity(QuestionDTO questionDTO);

    @Named("mapQuiz")
    Quiz mapQuiz(Long quizId) {
        return quizService.findQuizById(quizId);
    }

    @Mapping(target = "quiz", ignore = true)
    public abstract Question fromQuestionResponseDTOtoEntity(QuestionDTO questionDTO);

    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract void updateQuestionFromDTO(QuestionDTO questionDTO, @MappingTarget Question question);

    @Mapping(target = "quizId", source = "quiz", qualifiedByName = "mapToQuizId")
    public abstract QuestionDTO toDTO(Question question);

    @Named("mapToQuizId")
    public Long mapToQuizId(Quiz quiz) {
        return quiz.getId();
    }
}
