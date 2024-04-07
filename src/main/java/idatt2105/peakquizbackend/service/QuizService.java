package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.QuizNotFoundException;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.repository.QuizRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Quiz entities.
 */
@Service
@AllArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    /**
     * Save a quiz entity.
     * @param quiz The quiz to save
     * @return The saved quiz entity
     */
    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    /**
     * Find a quiz by its ID.
     * @param quizId The ID of the quiz to find
     * @return The found quiz entity
     * @throws QuizNotFoundException if the quiz is not found
     */
    public Quiz findQuizById(Long quizId) {
        return quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);
    }

    /**
     * Get a page of all quizzes.
     * @param pageable The pageable object for pagination
     * @return A page of quiz entities
     */
    public Page<Quiz> findAllQuizzes(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    /**
     * Get a page of quizzes with filters applied.
     * @param categoryIds The list of category IDs to filter by
     * @param pageable The pageable object for pagination
     * @return A page of filtered quiz entities
     */
    public Page<Quiz> findQuizzesWithFilters(List<Long> categoryIds, Pageable pageable) {
        return quizRepository.findAllFiltered(categoryIds, pageable);
    }

    /**
     * Get a page of all quiz templates.
     * @param pageable The pageable object for pagination
     * @return A page of quiz template entities
     */
    public Page<Quiz> findAllTemplates(Pageable pageable) {
        return quizRepository.findAllTemplates(pageable);
    }

    /**
     * Delete a quiz by its ID.
     * @param id The ID of the quiz to delete
     */
    public void deleteQuizById(Long id) {
        Quiz quiz = findQuizById(id);
        quizRepository.deleteById(quiz.getId());
    }
}
