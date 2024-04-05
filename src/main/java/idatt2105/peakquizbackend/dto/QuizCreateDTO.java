package idatt2105.peakquizbackend.dto;

import idatt2105.peakquizbackend.model.Question;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateDTO {
  private String title;
  private String description;
  private String imageUrl;
  private ZonedDateTime createdON;
  private Integer playCount;
  private Set<Question> questions;
  private Set<String> categoryName;
}
