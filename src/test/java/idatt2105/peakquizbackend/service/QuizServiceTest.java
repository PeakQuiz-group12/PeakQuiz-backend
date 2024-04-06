package idatt2105.peakquizbackend.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import idatt2105.peakquizbackend.exceptions.QuizNotFoundException;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.repository.QuizRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
public class QuizServiceTest {

  @TestConfiguration
  static class QuizServiceTestConfiguration {

    @Bean
    public QuizService quizService(QuizRepository quizRepository) {
      return new QuizService(quizRepository);
    }
  }

  @Autowired
  private QuizService quizService;

  @MockBean
  private QuizRepository quizRepository;

  Quiz existingQuiz;

  Quiz nonExistingQuiz;

  @Before
  public void setUp() {

    existingQuiz = new Quiz();
    existingQuiz.setId(1L);

    when(quizRepository.save(existingQuiz)).thenReturn(existingQuiz);
    when(quizRepository.findById(existingQuiz.getId())).thenReturn(Optional.of(existingQuiz));
    doNothing().when(quizRepository).deleteById(existingQuiz.getId());

    nonExistingQuiz = new Quiz();
    when(quizRepository.save(nonExistingQuiz)).thenReturn(nonExistingQuiz);
    when(quizRepository.findById(nonExistingQuiz.getId())).thenReturn(Optional.empty());
    doNothing().when(quizRepository).deleteById(nonExistingQuiz.getId());

    List<Quiz> content = new ArrayList<>();
    content.add(existingQuiz);


    when(quizRepository.findAll(PageRequest.of(0,3))).thenReturn(new PageImpl<>(content,PageRequest.of(0,3), 1));

  }

  @Test
  public void testSaveQuiz() {
    Quiz newQuiz = quizService.saveQuiz(nonExistingQuiz);

    assertEquals(newQuiz, nonExistingQuiz);
  }

  @Test
  public void testFindExistingQuizById() {
    Quiz quiz = quizService.findQuizById(existingQuiz.getId());
    assertEquals(existingQuiz, quiz);
  }

  @Test
  public void testFindNonExistingQuizById() {
    assertThrows(QuizNotFoundException.class, () -> quizService.findQuizById(nonExistingQuiz.getId()));
  }

  @Test
  public void testFindAllQuizzes() {
    assertEquals(1, quizService.findAllQuizzes(PageRequest.of(0,3)).getTotalElements());
  }

  @Test
  public void testDeleteQuizById() {
    try {
      quizService.deleteQuizById(existingQuiz.getId());
    } catch (Exception ex) {
      fail();
    }
  }

  @Test
  public void testNonExistentQuizById() {
    assertThrows(QuizNotFoundException.class, () -> {
      quizService.deleteQuizById(
          nonExistingQuiz.getId());
      fail();
    });
  }
}
