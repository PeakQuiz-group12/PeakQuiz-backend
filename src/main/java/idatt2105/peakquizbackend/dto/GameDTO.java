package idatt2105.peakquizbackend.dto;

import idatt2105.peakquizbackend.model.Game.GameId;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GameDTO {
  private GameId gameId;
  private Integer correctAnswers;
  private Byte rating;
  private String feedback;
  private ZonedDateTime playedOn;
  private String username;
  private Long quizId;
}
