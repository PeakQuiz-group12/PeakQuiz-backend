package idatt2105.peakquizbackend.mapper;

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

@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class QuizMapper {

  @Autowired
  private QuizService quizService;
  @Autowired
  private CategoryService categoryService;
  @Autowired
  private QuestionService questionService;
  @Autowired
  private QuestionMapper questionMapper;
  @Autowired
  private CollaborationService collaborationService;

  public static QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);


  @Mapping(target = "playCount", ignore = true)
  @Mapping(target = "questions", ignore = true)
  @Mapping(target = "games", ignore = true)
  @Mapping(target = "createdOn", ignore = true)
  @Mapping(target = "collaborators", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategories")
  public abstract Quiz fromQuizCreateDTOtoEntity(QuizCreateDTO quizCreateDTO);

  @Mapping(target = "games", ignore = true)
  @Mapping(target = "createdOn", ignore = true)
  @Mapping(target = "collaborators", ignore = true)
  @Mapping(target = "categories", ignore = true)
  @Mapping(target = "questions", ignore = true)
  @Mapping(target = "id", ignore = true)
  public abstract void updateQuizFromDTO(QuizResponseDTO quizResponseDTO, @MappingTarget Quiz quiz);


  @Named("getQuestions")
  public Set<Question> getQuestionsCreate(QuizResponseDTO quizResponseDTO) {
    return quizResponseDTO.getQuestions().stream()
            .map(question -> questionMapper.fromQuestionDTOtoEntity(question))
            .collect(Collectors.toSet());
  }

  @Named("getQuestions")
  public Page<Question> getQuestions(QuizResponseDTO responseDTO) {
    return questionService.findQuestionsByQuizId(responseDTO.getId(), PageRequest.of(0, Integer.MAX_VALUE));
  }

  @Named("getCategories")
  public Set<Category> getCategories(QuizResponseDTO responseDTO) {
    return categoryService.findCategoriesByQuizId(responseDTO.getId());
  }

  @Named("mapCategories")
  public Set<Category> mapCategories(Set<String> categoryNames) {
    return categoryNames.stream()
            .map(name -> categoryService.findCategoryByName(name)
            )
            .collect(Collectors.toSet());
  }

  @Named("mapCategoriesToNames")
  public Set<String> mapCategoriesToNames(Set<Category> categories) {
    return categories.stream()
            .map(Category::getName)
            .collect(Collectors.toSet());
  }

  @Named("getCategoryIds")
  public Set<Long> getCategoryIds(Quiz quiz) {
    return quiz.getCategories().stream().map(Category::getId).collect(Collectors.toSet());
  }

  @Named("getQuestionIds")
  public Set<Long> questionIds(Quiz quiz) {
    return quiz.getQuestions().stream().map(Question::getId).collect(Collectors.toSet());
  }

  @Named("mapCollaborators")
  public Set<String> mapCollaborators(Set<Collaboration> collaborators) {
    return collaborators.stream()
            .map(collaboration -> collaboration.getUser().getUsername())
            .collect(Collectors.toSet());
  }


  @Mapping(target = "collaboratorUsernames", source = "collaborators", qualifiedByName = "mapCollaborators")
  @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategoriesToNames")
  public abstract QuizResponseDTO toDTO(Quiz quiz);


}
