package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.sql.Blob;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Quiz {
  @Id
  @GenericGenerator(
          name = "QUIZ_ID_GENERATOR",
          strategy = "enhanced-sequence",
          parameters = {
                  @Parameter(
                          name = "sequence_name",
                          value = "QUIZ_SEQUENCE"
                  ),
                  @Parameter(
                          name = "initial_value",
                          value = "1000"
                  )
          })
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