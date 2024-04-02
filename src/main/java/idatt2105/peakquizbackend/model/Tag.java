package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.envers.NotAudited;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TAG")
public class Tag {

  @Id
  @GeneratedValue(
          generator = "tag_id_seq",
          strategy = GenerationType.SEQUENCE
  )
  @SequenceGenerator(
          name = "tag_id_seq",
          sequenceName = "tag_id_seq"
  )
  Long id;

  @NotNull
  @Column(nullable = false)
  @Size(
          min = 2,
          max = 10,
          message = "Title is required, maximum 10 characters."
  )
  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
          name = "USER_ID",
          nullable = false
  )
  private User user;

  @ManyToMany(cascade = CascadeType.DETACH)
  @JoinTable(
          name = "TAG_QUIZ",
          joinColumns = @JoinColumn(name = "TAG_ID"),
          inverseJoinColumns = @JoinColumn(name = "QUIZ_ID")
  )
  private Set<Quiz> quizzes = new HashSet<>();
}
