package idatt2105.peakquizbackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import idatt2105.peakquizbackend.repository.CollaborationRepository;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CollaborationServiceTest {

    @TestConfiguration
    static class CollaborationServiceConfiguration {

        @Bean
        public CollaborationService collaborationService(CollaborationRepository collaborationRepository) {
            return new CollaborationService(collaborationRepository);
        }
    }

    @Autowired
    private CollaborationService collaborationService;

    @MockBean
    private CollaborationRepository collaborationRepository;

    final User user = new User();

    final User otherUser = new User();
    final Quiz quiz = new Quiz();

    Collaboration existingCollaboration;

    Collaboration nonExistingCollaboration;

    @Before
    public void setup() {
        user.setId(1L);
        otherUser.setId(2L);
        quiz.setId(1L);

        existingCollaboration = new Collaboration(user, quiz, CollaboratorType.CREATOR);

        when(collaborationRepository.save(existingCollaboration)).thenReturn(existingCollaboration);
        when(collaborationRepository.findAllByQuizId(existingCollaboration.getQuiz().getId(), PageRequest.of(0, 3)))
                .thenReturn(new PageImpl<>(List.of(existingCollaboration), PageRequest.of(0, 3), 1));
        when(collaborationRepository.findAllByUserIdAndCollaboratorType(existingCollaboration.getUser().getId(),
                CollaboratorType.CREATOR, PageRequest.of(0, 3)))
                        .thenReturn(new PageImpl<>(List.of(existingCollaboration), PageRequest.of(0, 3), 1));

        nonExistingCollaboration = new Collaboration(otherUser, quiz, CollaboratorType.CO_AUTHOR);
        when(collaborationRepository.findAllByQuizId(nonExistingCollaboration.getQuiz().getId(), PageRequest.of(0, 3)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 3), 0));
        when(collaborationRepository.findAllByUserIdAndCollaboratorType(nonExistingCollaboration.getUser().getId(),
                CollaboratorType.CO_AUTHOR, PageRequest.of(0, 3)))
                        .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 3), 0));
    }

    @Test
    public void testSaveCollaboration() {
        Collaboration collaboration = collaborationService.saveCollaboration(existingCollaboration.getUser(),
                existingCollaboration.getQuiz(), existingCollaboration.getCollaboratorType());
        assertEquals(existingCollaboration, collaboration);
    }

    @Test
    public void testFindCollaboratorsWithUnusedQuizId() {
        assertEquals(0,
                collaborationService
                        .findCollaboratorsByQuizId(nonExistingCollaboration.getQuiz().getId(), PageRequest.of(0, 3))
                        .getTotalElements());
    }

    @Test
    public void testFindQuizzesByUserId() {
        assertEquals(1, collaborationService.findQuizzesByUserId(existingCollaboration.getUser().getId(),
                CollaboratorType.CREATOR, PageRequest.of(0, 3)).getTotalElements());
    }

    @Test
    public void testFindQuizzesByUnusedUserId() {
        assertEquals(0, collaborationService.findQuizzesByUserId(nonExistingCollaboration.getUser().getId(),
                CollaboratorType.CO_AUTHOR, PageRequest.of(0, 3)).getTotalElements());
    }
}
