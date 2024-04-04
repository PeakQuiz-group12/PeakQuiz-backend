package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.sql.Blob;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@Data
public class Quiz {
  @Id
  @GeneratedValue(generator = "quiz_id_seq", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
          name = "quiz_id_seq",
          sequenceName = "quiz_id_seq"
  )
  private Long id;

  private String title;

  private String description;

  // Blob is a class containing a stream of bytes. In this case the bytes represent an image.
  // alternatively we can use String:filename if we have access to a filesystem
  @Lob
  private Blob image;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false,
          updatable = false)
  @CreationTimestamp
  private ZonedDateTime createdOn;

  // TODO: Denne kan være games.size()?
  @NotAudited
  @Formula(
      "(SELECT COUNT(*) FROM GAME G WHERE G.QUIZ_ID = ID)"
  )
  private Integer playCount;

  @NotAudited
  @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER)
  private Set<Game> games = new HashSet<>();

  // Unidirectional mapping between quizzes and questions
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "quiz")
  private Set<Question> questions = new HashSet<>();

  @NotAudited
  @ManyToMany(mappedBy = "quizzes", fetch = FetchType.EAGER)
  private Set<Category> categories = new HashSet<>();

  @NotAudited
  @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER)
  private Set<Collaboration> collaborators = new HashSet<>();

  public void addQuestion(Question question) {
    this.questions.add(question);
  }

  public void removeQuestion(Question question) {
    this.questions.remove(question);
  }
}