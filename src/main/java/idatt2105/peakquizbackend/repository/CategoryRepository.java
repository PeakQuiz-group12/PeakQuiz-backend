package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Category;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing categories stored in the database.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Retrieves all categories associated with a specific quiz ID.
     *
     * @param quizId The ID of the quiz
     * @return A set of categories associated with the specified quiz ID
     */
    @Query("SELECT c FROM Category c JOIN c.quizzes q WHERE q.id = :quizId")
    Set<Category> findCategoriesByQuizId(Long quizId);

    /**
     * Searches for a category by its name.
     *
     * @param categoryName The name of the category
     * @return An optional containing the category if found, otherwise empty
     */
    Optional<Category> findCategoryByName(String categoryName);
}
