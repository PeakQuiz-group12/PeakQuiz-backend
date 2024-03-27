package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
public class Collaboration {

  @Embeddable
  public static class Id implements Serializable {
    @Column(name = "USER_ID")
    protected Long userId;

    @Column(name = "QUIZ_ID")
    protected Long quizId;

    public Id() {
    }

    public Id(Long userId, Long quizId) {
      this.userId = userId;
      this.quizId = quizId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Id id)) return false;
      return Objects.equals(userId, id.userId) && Objects.equals(quizId, id.quizId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(userId, quizId);
    }
  }

  @EmbeddedId
  protected Id id = new Id();

  @Column(nullable = false)
  @NotNull
  CollaboratorType collaboratorType;


  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false,
          updatable = false)
  @CreationTimestamp
  protected ZonedDateTime joinedOn;

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
}
