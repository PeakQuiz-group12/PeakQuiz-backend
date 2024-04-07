package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.GameDTO;
import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.model.Game.GameId;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Mapper interface for converting between Game entities and GameDTOs.
 */
@Mapper(componentModel = "spring")
public abstract class GameMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    /**
     * INSTANCE variable for accessing the mapper instance.
     */
    public static GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    /**
     * Converts a GameDTO to a Game entity.
     *
     * @param gameDTO
     *            The GameDTO to convert
     * @return The corresponding Game entity
     */
    @Mapping(source = "quizId", target = "quiz", qualifiedByName = "mapQuizToQuizId")
    @Mapping(source = "username", target = "user", qualifiedByName = "mapUserToUsername")
    @Mapping(source = "gameId", target = "id", qualifiedByName = "mapId")
    public abstract Game fromGameDTOtoEntity(GameDTO gameDTO);

    /**
     * Converts a Game entity to a GameDTO.
     *
     * @param game
     *            The Game entity to convert
     * @return The corresponding GameDTO
     */
    @Mapping(source = "quiz", target = "quizId", qualifiedByName = "mapQuiz")
    @Mapping(source = "user", target = "username", qualifiedByName = "mapUser")
    public abstract GameDTO toDTO(Game game);

    /**
     * Converts a Set of Game entities to a Set of GameDTOs.
     *
     * @param games
     *            The Set of Game entities to convert
     * @return The corresponding Set of GameDTOs
     */
    public abstract Set<GameDTO> toDTOs(Set<Game> games);

    /**
     * Maps the GameId to a new GameId object.
     *
     * @param gameId
     *            The GameId to map
     * @return The new GameId object
     */
    @Named("mapId")
    public GameId mapId(GameId gameId) {
        return new GameId();
    }

    /**
     * Maps the username to a User object.
     *
     * @param username
     *            The username to map
     * @return The corresponding User object
     */
    @Named("mapUserToUsername")
    public User mapUser(String username) {
        return userService.findUserByUsername(username);
    }

    /**
     * Maps the quizId to a Quiz object.
     *
     * @param id
     *            The quizId to map
     * @return The corresponding Quiz object
     */
    @Named("mapQuizToQuizId")
    public Quiz mapQuiz(Long id) {
        return quizService.findQuizById(id);
    }

    /**
     * Maps the Quiz object to its ID.
     *
     * @param quiz
     *            The Quiz object to map
     * @return The ID of the Quiz object
     */
    @Named("mapQuiz")
    public Long mapQuiz(Quiz quiz) {
        return quiz.getId();
    }

    /**
     * Maps the User object to its username.
     *
     * @param user
     *            The User object to map
     * @return The username of the User object
     */
    @Named("mapUser")
    public String mapUser(User user) {
        return user.getUsername();
    }
}
