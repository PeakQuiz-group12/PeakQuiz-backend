package idatt2105.peakquizbackend.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GameDTO {
  private Integer correctAnswers;
  private Byte rating;
  private String feedback;
  private ZonedDateTime playedOn;
  private String username;
  private Long quizId;
}
