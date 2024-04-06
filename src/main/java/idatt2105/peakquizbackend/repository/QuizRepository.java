package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q WHERE q.isTemplate = true")
    Page<Quiz> findAllTemplates(Pageable pageable);

    @Query("SELECT q FROM Quiz q " +
            "LEFT JOIN q.categories c " +
            "WHERE (:categoryIds IS NULL OR c.id IN (:categoryIds)) ")
    Page<Quiz> findAllFiltered(
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable);
}
