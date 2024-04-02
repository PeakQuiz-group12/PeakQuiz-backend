package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Blob;

@Entity
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
          message = "Username is required, maximum 20 characters."
  )
  @NotNull
  @Column(nullable = false)
  private String text;

  @Lob
  private Blob media;

  @Min(value = 0, message = "Difficulty should not be less than 0")
  @Max(value = 5, message = "Difficulty should not be greater than 5")
  @NotNull
  private Byte difficulty;

  private String explanation;


}
