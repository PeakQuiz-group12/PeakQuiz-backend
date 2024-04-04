package idatt2105.peakquizbackend.model;

import idatt2105.peakquizbackend.model.embedded.Comment;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Collaboration")
@Data
@NoArgsConstructor
public class Collaboration {

  public Collaboration(
      User user,
      Quiz quiz,
      CollaboratorType collaboratorType) {
    this.collaboratorType = collaboratorType;
    this.user = user;
    this.quiz = quiz;
    this.id.userId = user.getId();
    this.id.quizId = quiz.getId();
    user.getCollaborations().add(this);
    quiz.getCollaborators().add(this);
  }
  @Embeddable
  @EqualsAndHashCode
  public static class CollaborationId implements Serializable {
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "QUIZ_ID")
    private Long quizId;

    public CollaborationId() {
    }

    public CollaborationId(Long userId, Long quizId) {
      this.userId = userId;
      this.quizId = quizId;
    }
    @Override
    public String toString() { return "{userId: " + userId + "; quizId: " + quizId + "}"; }

  }

  @EmbeddedId
  private CollaborationId id = new CollaborationId();

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
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  protected User user;

  @ManyToOne
  @JoinColumn(
          name = "QUIZ_ID",
          insertable = false, updatable = false
  )
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Quiz quiz;

  // TODO: Unsure if this mapping will work.
  //  May have to make this mapping @ManyToOne and CascadeType.REMOVE.
  @ElementCollection
  @CollectionTable(name = "COMMENT")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Comment> comments = new HashSet<>();
}

