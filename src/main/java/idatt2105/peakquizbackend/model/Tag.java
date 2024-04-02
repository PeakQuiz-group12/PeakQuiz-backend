package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

  @OneToMany(
          mappedBy = "tag",
          fetch = FetchType.LAZY,
          cascade = CascadeType.DETACH
  )
  private Set<Quiz> quizzes = new HashSet<>();
}
