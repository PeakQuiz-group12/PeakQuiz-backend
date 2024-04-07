package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for Quiz entity.
 */
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    /**
     * Retrieves all templates with pagination.
     * 
     * @param pageable
     *            Pagination information
     * @return Page of Quiz entities representing templates
     */
    @Query("SELECT q FROM Quiz q WHERE q.isTemplate = true")
    Page<Quiz> findAllTemplates(Pageable pageable);

    /**
     * Retrieves quizzes based on filtering criteria with pagination.
     * 
     * @param categoryIds
     *            List of category IDs to filter the quizzes
     * @param pageable
     *            Pagination information
     * @return Page of Quiz entities filtered by category IDs
     */
    @Query("SELECT q FROM Quiz q " + "LEFT JOIN q.categories c "
            + "WHERE (:categoryIds IS NULL OR c.id IN (:categoryIds)) ")
    Page<Quiz> findAllFiltered(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);
}
