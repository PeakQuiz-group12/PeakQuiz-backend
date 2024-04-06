package idatt2105.peakquizbackend.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Embeddable
@Data
public class Answer {

  @NotNull
  @Column(nullable = false)
  String answer;

  @NotNull
  @Column(nullable = false)
  Boolean isAnswer;
}
