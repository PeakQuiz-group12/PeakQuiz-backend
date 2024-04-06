package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Immutable
@Getter
@NoArgsConstructor
public class Game {
  @Embeddable
  @EqualsAndHashCode
  @NoArgsConstructor
  public static class GameId implements Serializable {
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "QUIZ_ID")
    private Long quizId;

    public GameId(Long userId, Long quizId) {
      this.userId = userId;
      this.quizId = quizId;
    }
  }

  @EmbeddedId
  private GameId id = new GameId();

  @Column(updatable = false)
  @NotNull
  private Integer correctAnswers;

  @Column(updatable = false)
  private Byte rating;

  @Column(updatable = false)
  private String feedback;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false,
          updatable = false)
  @CreationTimestamp
  private ZonedDateTime playedOn;

  // missing like, im unsure about what it signifies

  @ManyToOne
  @JoinColumn(
          name = "USER_ID",
          insertable = false, updatable = false)
  private User user;

  @ManyToOne
  @JoinColumn(
          name = "QUIZ_ID",
          insertable = false, updatable = false
  )
  private Quiz quiz;

  public Game(Integer correctAnswers, Byte rating, String feedback, User user, Quiz quiz) {
    this.correctAnswers = correctAnswers;
    this.rating = rating;
    this.feedback = feedback;
    this.user = user;
    this.quiz = quiz;
    this.user.getGames().add(this);
  }
}
