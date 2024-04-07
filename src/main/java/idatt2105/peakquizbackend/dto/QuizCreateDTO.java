package idatt2105.peakquizbackend.dto;

import java.util.Set;
import lombok.Data;

/**
 * DTO received from frontend when creating quiz.
 */
@Data
public class QuizCreateDTO {
    private String title;
    private String description;
    private String imageUrl;
    private Set<String> categories;
}
