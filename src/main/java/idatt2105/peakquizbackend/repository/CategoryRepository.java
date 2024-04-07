package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Category;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c JOIN c.quizzes q WHERE q.id = :quizId")
    Set<Category> findCategoriesByQuizId(Long quizId);

    Optional<Category> findCategoryByName(String categoryName);
}
