package idatt2105.peakquizbackend.repository;

import static org.junit.jupiter.api.Assertions.*;

import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class QuestionRepositoryTest {

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private TestEntityManager entityManager;

  private Question question;

  @BeforeEach
  void setup() {
    // Given
    question = Question.builder()
        .text("Test question")
        .questionType(QuestionType.MULTIPLE_CHOICE)
        .difficulty((byte) 3)
        .build();

  }

  @Test
  public void testQuestionEntityMapping() {

    // When
    entityManager.persistAndFlush(question);
    Question persistedQuestion = questionRepository.findById(question.getId()).get();

    // Then
    assertNotNull(persistedQuestion.getId());
    assertEquals(question.getText(), persistedQuestion.getText());
    assertEquals(question.getQuestionType(), persistedQuestion.getQuestionType());
    assertEquals(question.getDifficulty(), persistedQuestion.getDifficulty());
    // Test other properties similarly
  }

  @Test
  public void testQuestionEntityValidation() {
    // Given
    Question invalidQuestion = Question.builder()
        .text("A") // Invalid text length
        .questionType(QuestionType.MULTIPLE_CHOICE)
        .difficulty((byte) 3)
        .build();

    // When & Then
    assertThrows(ConstraintViolationException.class, () -> {
      entityManager.persistAndFlush(invalidQuestion);
    });
  }

  @Test
  public void testQuestionQuizRelationship() {

    //Given
    Quiz quiz = new Quiz();
    question.setQuiz(quiz);

    // When
    entityManager.persistAndFlush(question);
    Question persistedQuestion = questionRepository.findById(question.getId()).get();

    // Then
    assertNotNull(persistedQuestion.getQuiz());
    assertEquals(quiz, persistedQuestion.getQuiz());
  }

}
