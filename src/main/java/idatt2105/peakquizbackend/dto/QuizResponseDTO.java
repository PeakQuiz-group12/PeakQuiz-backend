package idatt2105.peakquizbackend.dto;

import idatt2105.peakquizbackend.model.Question;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class QuizResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private ZonedDateTime createdOn;
    private Integer playCount;
    private Set<QuestionDTO> questions;
    private Set<String> categories;
    private Set<String> collaboratorUsernames;
}