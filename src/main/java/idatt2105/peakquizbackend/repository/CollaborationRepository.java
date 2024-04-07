package idatt2105.peakquizbackend.repository;

import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Collaboration.CollaborationId;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Collaboration entity.
 */
public interface CollaborationRepository extends JpaRepository<Collaboration, CollaborationId> {

    /**
     * Retrieves all collaborations for a given user ID and collaborator type.
     * 
     * @param userId
     *            ID of the user
     * @param type
     *            Collaborator type
     * @param pageable
     *            Pagination information
     * @return Page of Collaboration entities
     */
    Page<Collaboration> findAllByUserIdAndCollaboratorType(Long userId, CollaboratorType type, Pageable pageable);

    /**
     * Retrieves all collaborations for a given quiz ID.
     * 
     * @param quizId
     *            ID of the quiz
     * @param pageable
     *            Pagination information
     * @return Page of Collaboration entities
     */
    Page<Collaboration> findAllByQuizId(Long quizId, Pageable pageable);
}
