package idatt2105.peakquizbackend;

import idatt2105.peakquizbackend.model.Collaboration;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import idatt2105.peakquizbackend.repository.CollaborationRepository;
import idatt2105.peakquizbackend.repository.QuestionRepository;
import idatt2105.peakquizbackend.repository.QuizRepository;
import idatt2105.peakquizbackend.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PeakQuizBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(PeakQuizBackendApplication.class, args);
  }

  Logger LOGGER = LoggerFactory.getLogger(PeakQuizBackendApplication.class);
  @Transactional
  @Bean
  public CommandLineRunner run (/*QuizRepository quizRepository, QuestionRepository questionRepository,
      UserRepository userRepository, CollaborationRepository collaborationRepository*/) {
    return (args -> {
      /*Quiz quiz = new Quiz();
      Question question = Question.builder()
              .text("text")
              .questionType(QuestionType.FILL_IN_BLANK)
              .difficulty((byte) 1)
              .quiz(quiz)
              .build();
      quiz.addQuestion(question);
      LOGGER.info("Saving quiz");
      quizRepository.save(quiz);

      System.out.println(quizRepository.findById(1L));

      LOGGER.info("Creating questing");
      Question newQuestion = Question.builder()
          .text("text2")
          .questionType(QuestionType.FILL_IN_BLANK)
          .difficulty((byte) 1)
          .quiz(quizRepository.findById(1L).get()).build();
      LOGGER.info("Saved question in quiz");
      // New question is not cascaded as intended.
      // I suspect its because of too many things being done in a single transaction.
      // Temp fix: Manually save the new question.
      //questionRepository.save(newQuestion);
      quiz.addQuestion(newQuestion);

      quizRepository.save(quiz);

      LOGGER.info("Saving user");

      System.out.println(quizRepository.findAll());

      User user = new User("username", "xulr@hotmail.com", "password");
      userRepository.save(user);

      LOGGER.info("Saved user");
      System.out.println(userRepository.findByUsername("username"));
      Collaboration collaboration = new Collaboration(user, quiz, CollaboratorType.CREATOR);

      LOGGER.info("Saving collab");
      collaborationRepository.save(collaboration);

      System.out.println(collaborationRepository.findAll());

      LOGGER.info("saved collab");*/
    });
  }
}
