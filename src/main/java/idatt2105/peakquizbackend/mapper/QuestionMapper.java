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

@Mapper
public abstract class QuestionMapper {

  @Autowired
  private QuizService quizService;

  QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);


  @Mapping(target = "quiz", source = "quizId", qualifiedByName = "getQuiz")
  public abstract Question fromQuestionCreateDTOtoEntity(QuestionDTO questionDTO);

  @Named("getQuiz")
  Quiz getQuiz(QuestionDTO questionDTO) {
    return quizService.findQuizById(questionDTO.getQuizId());
  }

  @Mapping(target = "quiz", ignore = true)
  public abstract Question fromQuestionResponseDTOtoEntity(QuestionDTO questionDTO);
  @Mapping(target = "quiz", ignore = true)
  @Mapping(target = "id", ignore = true)
  public abstract void updateQuestionFromDTO(QuestionDTO questionDTO, @MappingTarget Question question);

  public abstract QuestionDTO toDTO(Question question);
}
