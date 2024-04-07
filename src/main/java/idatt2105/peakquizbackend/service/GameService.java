package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public List<Game> findGamesByUserId(Long userId) {
        return gameRepository.findByUserId(userId);
    }

    public List<Game> findGamesByQuizId(Long quizId) {
        return gameRepository.findByQuizId(quizId);
    }

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }
}
