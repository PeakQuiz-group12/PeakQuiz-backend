package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.QuizCreateDTO;
import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.service.*;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Mapper(componentModel = "spring")
public abstract class QuizMapper {

  private QuizService quizService;
  private CategoryService categoryService;
  private QuestionService questionService;
  private QuestionMapper questionMapper;
  private CollaborationService collaborationService;

  public static QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);


  @Mapping(target = "playCount", ignore = true)
  @Mapping(target = "questions", ignore = true)
  @Mapping(target = "games", ignore = true)
  @Mapping(target = "createdOn", ignore = true)
  @Mapping(target = "collaborators", ignore = true)
  @Mapping(target = "id", ignore = true)
  public abstract Quiz fromQuizCreateDTOtoEntity(QuizCreateDTO quizCreateDTO);



  @Mapping(target = "games", ignore = true)
  @Mapping(target = "createdOn", ignore = true)
  @Mapping(target = "collaborators", ignore = true)
  @Mapping(target = "categories", ignore = true)
  @Mapping(target = "id", ignore = true)
  public abstract void updateQuizFromDTO(QuizResponseDTO quizResponseDTO, @MappingTarget Quiz quiz);


  @Named("getQuestions")
  public Set<Question> getQuestionsCreate(QuizResponseDTO quizResponseDTO) {
    return quizResponseDTO.getQuestions().stream().map(question -> questionMapper.getQuiz(question));
  }

  @Named("getQuestionsCreate")
  public Set<Question> getQuestions(QuizResponseDTO responseDTO) {
    return questionService.findQuestionsByQuizId(responseDTO.getId());
  }

  @Named("getCategories")
  public Set<Category> getCategories(QuizResponseDTO responseDTO) {
    return categoryService.findCategoriesByQuizId(responseDTO.getId());
  }

  @Named("getCategoryIds")
  public Set<Long> getCategoryIds(Quiz quiz) {
    return quiz.getCategories().stream().map(Category::getId).collect(Collectors.toSet());
  }

  @Named("getQuestionIds")
  public Set<Long> questionIds(Quiz quiz) {
    return quiz.getQuestions().stream().map(Question::getId).collect(Collectors.toSet());
  }


  @Mapping(target = "categoryNames", source = "categories", qualifiedByName = "getCategories")
  @Mapping(target = "questionIds", source = "")
  @Mapping(target = "createdON", source = "")
  @Mapping(target = "categoryIds", source = "")
  public abstract QuizResponseDTO toDTO(Quiz quiz);


}
