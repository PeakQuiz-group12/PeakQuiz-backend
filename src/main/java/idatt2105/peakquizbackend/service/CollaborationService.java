package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.mapper.QuizMapper;
import idatt2105.peakquizbackend.mapper.UserMapper;
import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import idatt2105.peakquizbackend.repository.CollaborationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing collaborations between users and quizzes.
 */
@Service
@AllArgsConstructor
public class CollaborationService {

    private final CollaborationRepository collaborationRepository;

    /**
     * Saves a collaboration between a user and a quiz.
     * 
     * @param user
     *            The user involved in the collaboration
     * @param quiz
     *            The quiz involved in the collaboration
     * @param collaboratorType
     *            The type of collaboration
     * @return The saved collaboration
     */
    public Collaboration saveCollaboration(User user, Quiz quiz, CollaboratorType collaboratorType) {
        Collaboration collaboration = new Collaboration(user, quiz, collaboratorType);
        return collaborationRepository.save(collaboration);
    }

    /**
     * Finds collaborators by quiz ID.
     * 
     * @param quizId
     *            The ID of the quiz to find collaborators for
     * @param pageable
     *            Pagination information
     * @return Page of user DTOs who are collaborators on the specified quiz
     */
    public Page<UserDTO> findCollaboratorsByQuizId(Long quizId, Pageable pageable) {
        return collaborationRepository.findAllByQuizId(quizId, pageable)
                .map(collaboration -> UserMapper.INSTANCE.toDTO(collaboration.getUser()));
    }

    /**
     * Finds quizzes by user ID and collaborator type.
     * 
     * @param userId
     *            The ID of the user to find quizzes for
     * @param collaboratorType
     *            The type of collaboration
     * @param pageable
     *            Pagination information
     * @return Page of quiz response DTOs for quizzes associated with the specified user and collaborator type
     */
    public Page<QuizResponseDTO> findQuizzesByUserId(Long userId, CollaboratorType collaboratorType,
            Pageable pageable) {
        return collaborationRepository.findAllByUserIdAndCollaboratorType(userId, collaboratorType, pageable)
                .map(collaboration -> QuizMapper.INSTANCE.toDTO(collaboration.getQuiz()));
    }
}
