package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.GameDTO;
import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.model.Game.GameId;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class GameMapper {

  @Autowired
  private UserService userService;

  @Autowired
  private QuizService quizService;

  public static GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

  @Mapping(source = "quizId", target = "quiz", qualifiedByName = "mapQuizToQuizId")
  @Mapping(source = "username", target = "user", qualifiedByName = "mapUserToUsername")
  @Mapping(source = "gameId", target = "id", qualifiedByName = "mapId")
  public abstract Game fromGameDTOtoEntity(GameDTO gameDTO);

  @Mapping(source = "quiz", target = "quizId", qualifiedByName = "mapQuiz")
  @Mapping(source = "user", target = "username", qualifiedByName = "mapUser")
  public abstract GameDTO toDTO(Game game);

  public abstract Set<GameDTO> toDTOs(Set<Game> games);

  @Named("mapId")
  public GameId mapId(GameId gameId) {
      return new GameId();
  }
  @Named("mapUserToUsername")
  public User mapUser(String username) {
    return userService.findUserByUsername(username);
  }

  @Named("mapQuizToQuizId")
  public Quiz mapQuiz(Long id) {
    return quizService.findQuizById(id);
  }

  @Named("mapQuiz")
  public Long mapQuiz(Quiz quiz) {
    return quiz.getId();
  }

  @Named("mapUser")
  public String mapUser(User user) {
    return user.getUsername();
  }

}
