package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.QuestionNotFoundException;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing questions.
 */
@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * Finds questions by quiz ID.
     * 
     * @param quizId
     *            The ID of the quiz to find questions for
     * @param pageable
     *            Pagination information
     * @return Page of questions for the specified quiz ID
     */
    public Page<Question> findQuestionsByQuizId(Long quizId, Pageable pageable) {
        return questionRepository.findAllByQuizId(quizId, pageable);
    }

    /**
     * Finds a question by its ID.
     * 
     * @param questionId
     *            The ID of the question to find
     * @return The found question
     * @throws QuestionNotFoundException
     *             if no question with the given ID is found
     */
    public Question findQuestionById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
    }

    /**
     * Saves a question.
     * 
     * @param question
     *            The question to save
     * @return The saved question
     */
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    /**
     * Deletes a question by its ID.
     * 
     * @param id
     *            The ID of the question to delete
     */
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
