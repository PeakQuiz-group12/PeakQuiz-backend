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
     * 
     * @param game
     *            The game to save
     * @return The saved game entity
     */
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

}
