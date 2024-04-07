package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Collaboration.CollaborationId;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollaborationRepository extends JpaRepository<Collaboration, CollaborationId> {

    Page<Collaboration> findAllByUserIdAndCollaboratorType(Long userId, CollaboratorType type, Pageable pageable);

    Page<Collaboration> findAllByQuizId(Long quizId, Pageable pageable);
}
