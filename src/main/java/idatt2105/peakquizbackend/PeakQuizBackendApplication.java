package idatt2105.peakquizbackend;

import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import idatt2105.peakquizbackend.repository.QuestionRepository;
import idatt2105.peakquizbackend.repository.QuizRepository;
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
  @Bean
  @Transactional
  public CommandLineRunner run (QuizRepository quizRepository, QuestionRepository questionRepository) {
    return (args -> {
      Quiz quiz = new Quiz();

      LOGGER.info("Saving quiz");
      quizRepository.save(quiz);

      LOGGER.info("Creating questing");
      Question newQuestion = Question.builder()
          .text("text1")
          .questionType(QuestionType.FILL_IN_BLANK)
          .difficulty((byte) 1)
          .quiz(quizRepository.findById(1L).get()).build();

      LOGGER.info("Saved question in quiz");
      questionRepository.save(newQuestion);

      System.out.println(quizRepository.findAll());
    });
  }
}
