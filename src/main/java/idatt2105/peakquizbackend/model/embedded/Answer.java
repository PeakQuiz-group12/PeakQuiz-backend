package idatt2105.peakquizbackend.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

  @NotNull
  @Column(nullable = false)
  String answer;

  @NotNull
  @Column(nullable = false)
  Boolean isAnswer;
}
