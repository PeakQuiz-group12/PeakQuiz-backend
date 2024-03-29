package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Collaboration")
public class Collaboration {

  @Embeddable
  public static class Id implements Serializable {
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "QUIZ_ID")
    private Long quizId;

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
  private Id id = new Id();

  @Column(nullable = false)
  @NotNull
  @Enumerated(EnumType.STRING)
  private CollaboratorType collaboratorType;


  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false,
          updatable = false)
  @CreationTimestamp
  private ZonedDateTime joinedOn;

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
  private Quiz quiz;

  // TODO: Unsure if this mapping will work.
  //  May have to make this mapping @ManyToOne and CascadeType.REMOVE.
  @ElementCollection
  @CollectionTable(name = "COMMENT")
  private Set<Comment> comments = new HashSet<>();

  public Id getId() {
    return id;
  }

  public CollaboratorType getCollaboratorType() {
    return collaboratorType;
  }

  public ZonedDateTime getJoinedOn() {
    return joinedOn;
  }

  public User getUser() {
    return user;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  public Set<Comment> getComments() {
    return comments;
  }
}
