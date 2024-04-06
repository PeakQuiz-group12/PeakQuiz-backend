package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.GameDTO;
import idatt2105.peakquizbackend.dto.TagDTO;
import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.exceptions.BadInputException;
import idatt2105.peakquizbackend.mapper.GameMapper;
import idatt2105.peakquizbackend.mapper.TagMapper;
import idatt2105.peakquizbackend.mapper.UserMapper;
import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.GameService;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.UserService;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final GameService gameService;
  private final QuizService quizService;

  @Autowired
  GameMapper gameMapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  @GetMapping
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/games")
  public ResponseEntity<?> getAllGames() {
    Set<GameDTO> games = gameMapper.toDTOs(new HashSet<>(gameService.findAllGames()));
    return ResponseEntity.ok(games);
  }

  @GetMapping("/games/{username}")
  public ResponseEntity<Set<GameDTO>> getGames(
          @PathVariable String username
  )
  {
    LOGGER.info("Received request for games by user: " + username);
    Set<Game> games = userService.findUserByUsername(username).getGames();

    LOGGER.info("Successfully found games");

    Set<GameDTO> gameDTOs = gameMapper.toDTOs(games);


    return ResponseEntity.ok(gameDTOs);
  }

  @PostMapping("/games/{username}")
  public ResponseEntity<GameDTO> createGame(
      @PathVariable String username,
      @RequestBody @NonNull GameDTO gameDTO
  )
  {
    LOGGER.info("Received post request for game: {}", gameDTO);

    if (!username.equals(gameDTO.getUsername())) throw new BadInputException("Username mismatch");

    Game game = gameMapper.fromGameDTOtoEntity(gameDTO);
    game.getId().setUserId(game.getUser().getId());
    game.getId().setQuizId(game.getQuiz().getId());

    game.getUser().getGames().add(game);
    game.getQuiz().getGames().add(game);

    Game savedGame = gameService.saveGame(game);

    System.out.println("Added game to quiz and user");

    LOGGER.info("Successfully saved game");
    return ResponseEntity.ok(gameMapper.toDTO(savedGame));
  }

  @GetMapping("/tags/{username}")
  public ResponseEntity<Set<TagDTO>> getTags(
          @PathVariable String username
  ) {
    LOGGER.info("Received request for tags by user: " + username);
    User user = userService.findUserByUsername(username);
    Set<TagDTO> tags = user.getTags().stream().map(tag -> TagMapper.INSTANCE.toDTO(tag)).collect(Collectors.toSet());
    return ResponseEntity.ok(tags);
  }

  /*@PostMapping
  public ResponseEntity<TagDTO> createTag(
          @RequestBody @NonNull TagDTO tagDTO
  )
  {
  }*/


}