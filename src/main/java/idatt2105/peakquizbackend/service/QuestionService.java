package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.QuestionNotFoundException;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.repository.QuestionRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;

  // can delete
  public Page<Question> findQuestionsByQuizId(Long quizId, Pageable pageable) {
    return questionRepository.findAllByQuizId(quizId, pageable);
  }

  public Question findQuestionById(Long questionId) {
    return questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
  }

  public Question saveQuestion(Question question) {
    return questionRepository.save(question);
  }

  public void deleteQuestion(Long id) {
    questionRepository.deleteById(id);
  }
}
