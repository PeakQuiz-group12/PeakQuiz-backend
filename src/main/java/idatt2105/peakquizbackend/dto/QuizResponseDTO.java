package idatt2105.peakquizbackend.dto;


import idatt2105.peakquizbackend.model.Category;
import java.sql.Blob;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponseDTO {

  private Long id;
  private String title;
  private String description;
  private Blob image;
  private ZonedDateTime createdON;
  private Integer playCount;
  private Set<Long> questionIds;
  private Set<Category> categories;
}