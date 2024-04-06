package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

}
