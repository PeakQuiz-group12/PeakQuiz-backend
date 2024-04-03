package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.sql.Blob;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
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

  @NotAudited
  @OneToMany(mappedBy = "quiz")
  private Set<Game> games = new HashSet<>();

  // Unidirectional mapping between quizzes and questions
  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(
          name = "QUIZ_QUESTION",
          joinColumns = @JoinColumn(name = "quiz_id")
  )
  private Set<Question> questions = new HashSet<>();

  @NotAudited
  @ManyToMany(mappedBy = "quizzes")
  private Set<Category> categories = new HashSet<>();

  @NotAudited
  @OneToMany(mappedBy = "quiz")
  private Set<Collaboration> collaborators = new HashSet<>();
  public Set<Game> getGames() {
    return games;
  }
}