package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

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
  protected Long id;

  protected String title;

  protected String description;

  // Blob is a class containing a stream of bytes. In this case the bytes represent an image.
  // alternatively we can use String:filename if we have access to a filesystem
  @Lob
  protected Blob image;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false,
          updatable = false)
  @CreationTimestamp
  protected ZonedDateTime createdOn;

  @OneToMany(mappedBy = "quiz")
  protected Set<Game> games = new HashSet<>();

  @OneToMany(mappedBy = "quiz")
  protected Set<Collaboration> collaborators = new HashSet<>();
  public Set<Game> getGames() {
    return games;
  }
}