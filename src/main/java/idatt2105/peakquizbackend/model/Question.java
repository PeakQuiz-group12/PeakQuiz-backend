package idatt2105.peakquizbackend.model;

import idatt2105.peakquizbackend.model.embedded.Answer;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "QUESTION")
@Builder
@Data
@Audited
@NoArgsConstructor
@AllArgsConstructor
public class Question {

  @Id
  @GeneratedValue(generator = "question_id_seq",
          strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
          name = "question_id_seq",
          sequenceName = "question_id_seq"
  )
  private Long id;

  @Size(
          min = 2,
          max = 20,
          message = "Text is required, maximum 20 characters."
  )
  @NotNull
  @Column(nullable = false)
  private String text;

  @NotNull
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private QuestionType questionType;

  @Lob
  private Blob media;

  @Min(value = 0, message = "Difficulty should not be less than 0")
  @Max(value = 5, message = "Difficulty should not be greater than 5")
  @NotNull
  private Byte difficulty;

  private String explanation;

  @ElementCollection
  @CollectionTable(name = "COMMENT")
  private Set<Answer> answers = new HashSet<>();

  @ManyToOne
  @NotNull
  private Quiz quiz;
}
