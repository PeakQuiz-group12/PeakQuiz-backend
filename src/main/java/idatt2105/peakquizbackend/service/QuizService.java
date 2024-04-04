package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.QuizAlreadyExistsException;
import idatt2105.peakquizbackend.exceptions.QuizNotFoundException;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.repository.QuizRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuizService {

  private final QuizRepository quizRepository;

  public Quiz saveQuiz(Quiz quiz) {
    return quizRepository.save(quiz);
  }

  public Quiz findQuizById(Long quizId) {
    return quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);
  }

  public Page<Quiz> findAllQuizzes(Pageable pageable) {
    return quizRepository.findAll(pageable);
  }

  public void deleteQuizById(Long id) {
    Quiz quiz = findQuizById(id);

    quizRepository.deleteById(quiz.getId());
  }
}
