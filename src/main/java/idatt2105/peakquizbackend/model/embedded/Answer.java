package idatt2105.peakquizbackend.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Embedded class representing an answer to a question.
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    /**
     * The answer text.
     */
    @NotNull
    @Column(nullable = false)
    private String answer;

    /**
     * Flag indicating whether the answer is correct or not.
     */
    @NotNull
    @Column(nullable = false)
    private Boolean isAnswer;
}
