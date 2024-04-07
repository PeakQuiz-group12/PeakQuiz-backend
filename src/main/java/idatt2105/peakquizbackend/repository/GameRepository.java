package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Game.GameId> {

    public List<Game> findByUserId(Long userId);

    public List<Game> findByQuizId(Long quizId);

}