package idatt2105.peakquizbackend.dto;

import idatt2105.peakquizbackend.model.embedded.Answer;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a question.
 */
@Data
public class QuestionDTO {

    /**
     * The ID of the question.
     */
    private Long id;

    /**
     * The text of the question.
     */
    private String text;

    /**
     * The type of the question.
     */
    private QuestionType questionType;

    /**
     * The media associated with the question.
     */
    private String media;

    /**
     * The difficulty of the question.
     */
    private Byte difficulty;

    /**
     * The explanation for the question.
     */
    private String explanation;

    /**
     * Set of answers associated with the question.
     */
    private Set<Answer> answers = new HashSet<>();

    /**
     * The ID of the quiz to which the question belongs.
     */
    private Long quizId;
}
