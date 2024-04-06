package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.GameDTO;
import idatt2105.peakquizbackend.mapper.GameMapper;
import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.service.GameService;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
          @RequestBody @NonNull GameDTO gameDTO
  )
  {
    LOGGER.info("Received post request for game: {}", gameDTO);
    Game game = gameMapper.fromGameDTOtoEntity(gameDTO);
    gameService.saveGame(game);
    game.getUser().getGames().add(game);
    game.getQuiz().getGames().add(game);
    userService.saveUser(game.getUser());
    quizService.saveQuiz(game.getQuiz());
    LOGGER.info("Successfully saved game");
    return ResponseEntity.ok(gameMapper.toDTO(game));
  }

}