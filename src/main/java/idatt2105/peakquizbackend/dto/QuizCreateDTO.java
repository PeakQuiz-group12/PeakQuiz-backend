package idatt2105.peakquizbackend.dto;

import java.time.ZonedDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO received from frontend when creating quiz.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateDTO {
  private String title;
  private String description;
  private String imageUrl;
  private Set<String> categories;
}
