package idatt2105.peakquizbackend.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Answer {

  @NotNull
  @Column(nullable = false)
  String answer;

  @NotNull
  @Column(nullable = false)
  Boolean isAnswer;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Answer answer1)) return false;

    if (!answer.equals(answer1.answer)) return false;
    return isAnswer.equals(answer1.isAnswer);
  }

  @Override
  public int hashCode() {
    int result = answer.hashCode();
    result = 31 * result + isAnswer.hashCode();
    return result;
  }
}
