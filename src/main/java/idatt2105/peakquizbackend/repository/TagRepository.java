package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByUserIdAndTitle(Long userId, String title);
}
