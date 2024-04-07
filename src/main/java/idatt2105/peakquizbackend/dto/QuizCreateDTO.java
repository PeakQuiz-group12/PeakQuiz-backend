package idatt2105.peakquizbackend.dto;

import java.util.Set;
import lombok.Data;

/**
 * Data Transfer Object (DTO) received from the frontend when creating a quiz.
 */
@Data
public class QuizCreateDTO {

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
     * Set of categories associated with the quiz.
     */
    private Set<String> categories;
}
