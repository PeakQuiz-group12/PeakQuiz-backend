package idatt2105.peakquizbackend.repository;

import static org.junit.jupiter.api.Assertions.*;

import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
public class CollaborationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CollaborationRepository collaborationRepository;

    private Collaboration collaboration;

    private Quiz quiz;
    private User user;

    @BeforeEach
    void setup() {
        user = new User("testUser", "test@example.com", "password");
        entityManager.persist(user);
        quiz = new Quiz();
        entityManager.persist(quiz);
        collaboration = new Collaboration(user, quiz, CollaboratorType.CREATOR);
        entityManager.persistAndFlush(collaboration);
    }

    @Test
    public void testCollaborationEntityMapping() {
        Collaboration persistedCollaboration = collaborationRepository.findById(collaboration.getId()).get();

        // Then
        assertNotNull(persistedCollaboration.getId());
        assertEquals(user, persistedCollaboration.getUser());
        assertEquals(quiz, persistedCollaboration.getQuiz());
        assertEquals(CollaboratorType.CREATOR, persistedCollaboration.getCollaboratorType());
    }

    @Test
    void testFindAllByUserIdAndCollaboratorType() {
        User user1 = new User("test", "test@mail.com", "pass");
        entityManager.persistAndFlush(user1);

        Collaboration collaboration1 = new Collaboration(user1, quiz, CollaboratorType.CO_AUTHOR);
        entityManager.persistAndFlush(collaboration1);

        Page<Collaboration> collaborations = collaborationRepository.findAllByUserIdAndCollaboratorType(user.getId(),
                CollaboratorType.CREATOR, PageRequest.of(0, 3));

        assertEquals(1, collaborations.getTotalElements());
    }

    @Test
    void testFindAllByQuizId() {
        User user1 = new User("test", "test@mail.com", "pass");
        entityManager.persist(user1);

        Collaboration collaboration1 = new Collaboration(user1, quiz, CollaboratorType.CO_AUTHOR);
        entityManager.persist(collaboration1);

        Page<Collaboration> collaborations = collaborationRepository.findAllByQuizId(quiz.getId(),
                PageRequest.of(0, 3));

        assertEquals(2, collaborations.getTotalElements());
    }
}
