package idatt2105.peakquizbackend.dto;

import java.time.ZonedDateTime;
import java.util.Set;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for quiz response information.
 */
@Data
public class QuizResponseDTO {

    /**
     * The unique identifier of the quiz.
     */
    private Long id;

    /**
     * The title of the quiz.
     */
    private String title;

    /**
     * The description of the quiz.
     */
    private String description;

    /**
     * The URL of the image associated with the quiz.
     */
    private String imageUrl;

    /**
     * The date and time when the quiz was created.
     */
    private ZonedDateTime createdOn;

    /**
     * The number of times the quiz has been played.
     */
    private Integer playCount;

    /**
     * Set of questions associated with the quiz.
     */
    private Set<QuestionDTO> questions;

    /**
     * Set of categories associated with the quiz.
     */
    private Set<String> categories;

    /**
     * Set of usernames of collaborators who contributed to the quiz.
     */
    private Set<String> collaboratorUsernames;
}
