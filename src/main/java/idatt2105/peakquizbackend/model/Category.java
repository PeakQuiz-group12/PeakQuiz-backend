package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
@Data
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
  @Column(nullable = false, unique = true)
  @Size(
          min = 2,
          max = 20,
          message = "Name is required, maximum 20 characters."
  )
  private String name;

  // Bidirectional mapping between Category and Quiz
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(
          name = "CATEGORY_QUIZ",
          joinColumns = @JoinColumn(name = "CATEGORY_ID"),
          inverseJoinColumns = @JoinColumn(name = "QUIZ_ID")
  )
  private Set<Quiz> quizzes = new HashSet<>();

  public void addQuiz(Quiz quiz) {
    this.quizzes.add(quiz);
    quiz.getCategories().add(this);
  }
  public void removeQuiz(Quiz quiz) {
    this.quizzes.remove(quiz);
    quiz.getCategories().remove(this);
  }

  public Category() {

  }
}
