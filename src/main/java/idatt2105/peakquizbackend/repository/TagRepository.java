package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Tag entity.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Find a tag by user ID and title.
     * @param userId The ID of the user associated with the tag
     * @param title The title of the tag
     * @return An Optional containing the tag if found, or an empty Optional otherwise
     */
    Optional<Tag> findTagByUserIdAndTitle(Long userId, String title);
}
