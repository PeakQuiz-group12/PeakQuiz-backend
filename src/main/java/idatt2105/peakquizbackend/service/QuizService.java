package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.repository.QuizRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuizService {

  private final QuizRepository quizRepository;

  public Quiz saveQuiz(Quiz quiz) {
    return quizRepository.save(quiz);
  }

  public Optional<Quiz> findQuizById(Long quizId) {
    return quizRepository.findById(quizId);
  }
}
