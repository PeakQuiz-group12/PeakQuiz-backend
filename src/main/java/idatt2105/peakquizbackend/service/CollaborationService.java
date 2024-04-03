package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.mappers.QuizMapper;
import idatt2105.peakquizbackend.mappers.UserMapper;
import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import idatt2105.peakquizbackend.repository.CollaborationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CollaborationService {

  private final CollaborationRepository collaborationRepository;
  private final UserMapper userMapper;
  private final QuizMapper quizMapper;

  public Collaboration saveCollaboration(User user,
      Quiz quiz,
      CollaboratorType collaboratorType) {
    Collaboration collaboration = new Collaboration(user, quiz, collaboratorType);
    return collaborationRepository.save(collaboration);
  }

  public Page<UserDTO> findCollaboratorsByQuizId(Long quizId, Pageable pageable) {
    return collaborationRepository.findAllByQuizId(quizId, pageable)
        .map(collaboration -> userMapper.toDTO(collaboration.getUser()));
  }

  public Page<QuizResponseDTO> findQuizzesByUserId(Long userId, CollaboratorType collaboratorType, Pageable pageable) {
    return collaborationRepository.findAllByUserIdAndCollaboratorType(userId, collaboratorType, pageable)
        .map(collaboration -> quizMapper.toDTO(collaboration.getQuiz()));
  }
}
