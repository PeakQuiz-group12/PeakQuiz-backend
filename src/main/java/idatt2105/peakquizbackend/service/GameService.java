package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Game entities.
 */
@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    /**
     * Save a game entity.
     * @param game The game to save
     * @return The saved game entity
     */
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    /**
     * Find games by user ID.
     * @param userId The ID of the user
     * @return A list of game entities associated with the user
     */
    public List<Game> findGamesByUserId(Long userId) {
        return gameRepository.findByUserId(userId);
    }

    /**
     * Find games by quiz ID.
     * @param quizId The ID of the quiz
     * @return A list of game entities associated with the quiz
     */
    public List<Game> findGamesByQuizId(Long quizId) {
        return gameRepository.findByQuizId(quizId);
    }

    /**
     * Get a list of all game entities.
     * @return A list of all game entities
     */
    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }
}
