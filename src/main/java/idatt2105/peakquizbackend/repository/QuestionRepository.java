package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Question entity.
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * Retrieves all questions associated with a quiz ID with pagination.
     * 
     * @param quizId
     *            ID of the quiz to retrieve questions for
     * @param pageable
     *            Pagination information
     * @return Page of Question entities associated with the quiz ID
     */
    Page<Question> findAllByQuizId(Long quizId, Pageable pageable);
}
