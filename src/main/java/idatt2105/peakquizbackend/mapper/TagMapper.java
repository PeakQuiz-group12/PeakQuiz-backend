package idatt2105.peakquizbackend.mapper;

import com.sun.mail.util.QEncoderStream;
import idatt2105.peakquizbackend.dto.QuestionDTO;
import idatt2105.peakquizbackend.dto.TagDTO;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Tag;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class TagMapper {

  @Autowired
  private QuestionMapper questionMapper;
  @Autowired
  private UserService userService;

  public static TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

  @Mapping(target = "user", source = "username", qualifiedByName = "mapUser")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "questions", source = "questions", qualifiedByName = "mapQuestions")
  @Mapping(target = "id", source = "id")
  public abstract Tag fromTagDTOtoEntity(TagDTO tagDTO);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "username", source = "user", qualifiedByName = "mapToUsername")
  @Mapping(target = "questions", source = "questions", qualifiedByName = "mapToQuestionDTOs")
  public abstract TagDTO toDTO(Tag tag);


  @Mapping(target = "user", source = "username", qualifiedByName = "mapUser")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "questions", source = "questions", qualifiedByName = "mapQuestions")
  @Mapping(target = "id", source = "id")
  public abstract void updateTagFromDTO(TagDTO tagDTO, @MappingTarget Tag tag);

  @Named("mapToQuestionDTOs")
  public Set<QuestionDTO> mapsToQuestionDTOs(Set<Question> questions) {
    return questions.stream().map(QuestionMapper.INSTANCE::toDTO).collect(Collectors.toSet());
  }

  @Named("mapToUsername")
  public String mapToUsername(User user) {
    return user.getUsername();
  }

  @Named("mapUser")
  public User mapUser(String username) {

    return userService.findUserByUsername(username);
  }

  @Named("mapQuestions")
  public Set<Question> mapQuestions(Set<QuestionDTO> questions) {
    return questions.stream().map(questionMapper::fromQuestionResponseDTOtoEntity).collect(Collectors.toSet());
  }


}
