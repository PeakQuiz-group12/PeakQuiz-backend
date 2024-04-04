package idatt2105.peakquizbackend.dto;

import idatt2105.peakquizbackend.model.embedded.Answer;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class QuestionCreateDTO {

  private String text;

  private QuestionType questionType;

  private Blob media;

  private Byte difficulty;

  private String explanation;

  private Set<Answer> answers = new HashSet<>();

  private Long quizId;
}
