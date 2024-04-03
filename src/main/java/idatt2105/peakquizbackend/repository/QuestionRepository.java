package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  Page<Question> findAllByQuizId(Long quizId, Pageable pageable);
}
