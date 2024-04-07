package idatt2105.peakquizbackend.dto;

import idatt2105.peakquizbackend.model.Game.GameId;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * Data Transfer Object (DTO) representing a game.
 */
@Data
public class GameDTO {

    /**
     * The ID of the game.
     */
    private GameId gameId;

    /**
     * The number of correct answers in the game.
     */
    private Integer correctAnswers;

    /**
     * The rating of the game.
     */
    private Byte rating;

    /**
     * Feedback associated with the game.
     */
    private String feedback;

    /**
     * The timestamp when the game was played.
     */
    private ZonedDateTime playedOn;

    /**
     * The username of the user associated with the game.
     */
    private String username;

    /**
     * The ID of the quiz associated with the game.
     */
    private Long quizId;
}
