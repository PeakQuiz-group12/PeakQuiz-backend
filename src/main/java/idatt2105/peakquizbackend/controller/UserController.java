package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.dto.GameDTO;
import idatt2105.peakquizbackend.dto.TagDTO;
import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.exceptions.BadInputException;
import idatt2105.peakquizbackend.mapper.GameMapper;
import idatt2105.peakquizbackend.mapper.TagMapper;
import idatt2105.peakquizbackend.mapper.UserMapper;
import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.model.Tag;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.GameService;
import idatt2105.peakquizbackend.service.TagService;
import idatt2105.peakquizbackend.service.UserService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller class handling User-related endpoints.
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final GameService gameService;
    private final TagService tagService;

    @Autowired
    private final TagMapper tagMapper;

    @Autowired
    GameMapper gameMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing a list of UserDTOs.
     */
    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }) })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.findAllUsers();
        List<UserDTO> userDTOS = users.stream().map(UserMapper.INSTANCE::toDTO).toList();
        return ResponseEntity.ok(userDTOS);
    }

    /**
     * Retrieves games associated with a user.
     *
     * @param username
     *            Username of the user.
     * @return ResponseEntity containing a set of GameDTOs.
     */
    @Operation(summary = "Get user's games", description = "Retrieves games associated with a user.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved games", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = GameDTO.class)) }) })
    @GetMapping("/{username}/games")
    public ResponseEntity<Set<GameDTO>> getGames(
            @Parameter(description = "Username of the user to retrieve games for", required = true) @PathVariable String username) {
        LOGGER.info("Received request for games by user: {}", username);
        Set<Game> games = userService.findUserByUsername(username).getGames();

        LOGGER.info("Successfully found games");

        Set<GameDTO> gameDTOs = gameMapper.toDTOs(games);
        return ResponseEntity.ok(gameDTOs);
    }

    /**
     * Creates a new game for a user.
     *
     * @param username
     *            Username of the user.
     * @param gameDTO
     *            Game data to create.
     * @return ResponseEntity containing the created GameDTO.
     */
    @Operation(summary = "Create game for user", description = "Creates a new game for a user.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully created game", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = GameDTO.class)) }) })
    @PostMapping("/{username}/games")
    public ResponseEntity<GameDTO> createGame(
            @Parameter(description = "Username of the user to create a game for", required = true) @PathVariable String username,
            @Parameter(description = "Game data to create", required = true) @RequestBody @NonNull GameDTO gameDTO) {
        LOGGER.info("Received post request for game: {}", gameDTO);

        if (!username.equals(gameDTO.getUsername()))
            throw new BadInputException("Username mismatch");

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

    /**
     * Retrieves tags associated with a user.
     *
     * @param username
     *            Username of the user.
     * @return ResponseEntity containing a set of TagDTOs.
     */
    @Operation(summary = "Get user's tags", description = "Retrieves tags associated with a user.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved tags", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class)) }) })
    @GetMapping("/{username}/tags")
    public ResponseEntity<Set<TagDTO>> getTags(
            @Parameter(description = "Username of the user to retrieve tags for", required = true) @PathVariable String username) {
        LOGGER.info("Received get request for tags by user: {}", username);
        User user = userService.findUserByUsername(username);
        Set<TagDTO> tags = user.getTags().stream().map(tagMapper::toDTO).collect(Collectors.toSet());
        return ResponseEntity.ok(tags);
    }

    /**
     * Creates a new tag for a user.
     *
     * @param username
     *            Username of the user.
     * @param tagDTO
     *            Tag data to create.
     * @return ResponseEntity containing the created TagDTO.
     */
    @Operation(summary = "Create tag for user", description = "Creates a new tag for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid tag format") })
    @PostMapping("/{username}/tags")
    public ResponseEntity<TagDTO> createTag(
            @Parameter(description = "Username of the user to create a tag for", required = true) @PathVariable String username,
            @Parameter(description = "Tag data to create", required = true) @RequestBody @NonNull TagDTO tagDTO) {
        LOGGER.info("Received post request for tag: {}", tagDTO);
        User user = userService.findUserByUsername(username);
        Tag tag = tagMapper.fromTagDTOtoEntity(tagDTO);

        Tag persistedTag = tagService.saveTag(tag);
        user.getTags().add(persistedTag);
        userService.saveUser(user);
        return ResponseEntity.ok(tagMapper.toDTO(persistedTag));
    }

    /**
     * Updates an existing tag for a user.
     *
     * @param username
     *            Username of the user.
     * @param tagDTO
     *            Updated tag data.
     * @return ResponseEntity containing the updated TagDTO.
     */
    @Operation(summary = "Update tag for user", description = "Updates an existing tag for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated tag", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Tag not found") })
    @PutMapping("/{username}/tags")
    public ResponseEntity<TagDTO> updateTag(
            @Parameter(description = "Username of the user to update a tag for", required = true) @PathVariable String username,
            @Parameter(description = "Updated tag data", required = true) @RequestBody TagDTO tagDTO) {
        LOGGER.info("Received put request for tag: {}", tagDTO);
        Tag tag = tagService.findTagById(tagDTO.getId());
        tagMapper.updateTagFromDTO(tagDTO, tag);
        Tag updatedTag = tagService.saveTag(tag);
        User user = userService.findUserByUsername(username);
        user.getTags().add(updatedTag);
        userService.saveUser(user);
        return ResponseEntity.ok(TagMapper.INSTANCE.toDTO(updatedTag));
    }
}
