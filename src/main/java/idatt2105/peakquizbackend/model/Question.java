package idatt2105.peakquizbackend.model;

import idatt2105.peakquizbackend.model.embedded.Answer;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.URL;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a question in a quiz.
 */
@Entity
@Table(name = "QUESTION")
@Data
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(generator = "question_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "question_id_seq", sequenceName = "question_id_seq")
    private Long id;

    @Size(min = 2, max = 100, message = "Text is required, maximum 100 characters.")
    @NotNull
    @Column(nullable = false)
    private String text;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(columnDefinition = "TEXT", length = 2048)
    @URL(regexp = "(?i)^(http|https):\\/\\/.+\\.(jpg|jpeg|png|gif)$", message = "Invalid image URL format. Must start with http/https and be of type .jpg, .jpeg, .png or .gif")
    private String media;

    @Min(value = 0, message = "Difficulty should not be less than 0")
    @Max(value = 5, message = "Difficulty should not be greater than 5")
    @NotNull
    private Byte difficulty;

    private String explanation;

    @ElementCollection
    @CollectionTable(name = "ANSWER")
    private Set<Answer> answers = new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Quiz quiz;
}
