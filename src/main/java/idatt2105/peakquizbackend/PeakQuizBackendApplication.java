package idatt2105.peakquizbackend;

import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import idatt2105.peakquizbackend.repository.QuestionRepository;
import idatt2105.peakquizbackend.repository.QuizRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PeakQuizBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(PeakQuizBackendApplication.class, args);
  }

  @Bean
  @Transactional
  public CommandLineRunner run (QuizRepository quizRepository, QuestionRepository questionRepository) {
    return (args -> {
      Quiz quiz = new Quiz();

      HashSet<Question> questions = new HashSet<>();
      Question question = Question.builder()
          .text("text")
          .questionType(QuestionType.FILL_IN_BLANK)
          .difficulty((byte) 1)
          .build();
      questions.add(question);
      quiz.setQuestions(questions);

      quizRepository.save(quiz);

      Question newQuestion = Question.builder()
          .text("text1")
          .questionType(QuestionType.FILL_IN_BLANK)
          .difficulty((byte) 1)
          .quiz(quizRepository.findById(1L).get()).build();

      questionRepository.save(newQuestion);

      System.out.println(quizRepository.findAll());
    });
  }
}
