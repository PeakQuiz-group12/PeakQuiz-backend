package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Immutable
@NoArgsConstructor
public class Game {
  @Embeddable
  public static class GameId implements Serializable {
    @Column(name = "USER_ID")
    protected Long userId;

    @Column(name = "QUIZ_ID")
    protected Long quizId;

    public GameId() {
    }

    public GameId(Long userId, Long quizId) {
      this.userId = userId;
      this.quizId = quizId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof GameId id)) return false;
      return Objects.equals(userId, id.userId) && Objects.equals(quizId, id.quizId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(userId, quizId);
    }
  }

  @EmbeddedId
  protected GameId id = new GameId();

  @Column(updatable = false)
  @NotNull
  protected Integer correctAnswers;

  @Column(updatable = false)
  protected Byte rating;

  @Column(updatable = false)
  protected String feedback;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false,
          updatable = false)
  @CreationTimestamp
  protected ZonedDateTime playedOn;

  // missing like, im unsure about what it signifies

  @ManyToOne
  @JoinColumn(
          name = "USER_ID",
          insertable = false, updatable = false)
  protected User user;

  @ManyToOne
  @JoinColumn(
          name = "QUIZ_ID",
          insertable = false, updatable = false
  )
  protected Quiz quiz;

  public Game(Integer correctAnswers, Byte rating, String feedback, User user, Quiz quiz) {
    this.correctAnswers = correctAnswers;
    this.rating = rating;
    this.feedback = feedback;
    this.user = user;
    this.quiz = quiz;
    this.user.getGames().add(this);
  }
}
