package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("SELECT c FROM Category c JOIN c.quizzes q WHERE q.id = :quizId")
  List<Category> findCategoriesByQuizId(Long quizId);
}
