package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.envers.NotAudited;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
public class Category {

  @Id
  @GeneratedValue(
          generator = "category_id_seq",
          strategy = GenerationType.SEQUENCE
  )
  @SequenceGenerator(
          name = "category_id_seq",
          sequenceName = "category_id_seq"
  )
  Long id;

  @NotNull
  @Column(nullable = false)
  @Size(
          min = 2,
          max = 20,
          message = "Name is required, maximum 20 characters."
  )
  private String name;

  // Bidirectional mapping between Category and Quiz
  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(
          name = "CATEGORY_QUIZ",
          joinColumns = @JoinColumn(name = "CATEGORY_ID"),
          inverseJoinColumns = @JoinColumn(name = "QUIZ_ID")
  )
  private Set<Quiz> quizzes = new HashSet<>();
}
