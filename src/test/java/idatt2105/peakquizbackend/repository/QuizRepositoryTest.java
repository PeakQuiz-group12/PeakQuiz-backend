package idatt2105.peakquizbackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import idatt2105.peakquizbackend.model.Quiz;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class QuizRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private QuizRepository quizRepository;

  @Before
  public void setup() {

  }
  @Test
  public void testFindById() {
    Quiz quiz = new Quiz();
    quiz.setDescription("hi");

    entityManager.persistAndFlush(quiz);
    entityManager.flush();

    Quiz foundQuiz = quizRepository.findById(quiz.getId()).get();

    assertEquals(quiz.getDescription(), foundQuiz.getDescription());
    assertEquals(0, foundQuiz.getQuestions().size());
  }
}
