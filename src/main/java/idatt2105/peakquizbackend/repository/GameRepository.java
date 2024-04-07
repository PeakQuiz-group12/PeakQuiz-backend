package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Game entity.
 */
public interface GameRepository extends JpaRepository<Game, Game.GameId> {

    /**
     * Retrieves all games associated with a user ID.
     * 
     * @param userId
     *            ID of the user
     * @return List of Game entities associated with the user ID
     */
    List<Game> findByUserId(Long userId);

    /**
     * Retrieves all games associated with a quiz ID.
     * 
     * @param quizId
     *            ID of the quiz
     * @return List of Game entities associated with the quiz ID
     */
    List<Game> findByQuizId(Long quizId);
}
