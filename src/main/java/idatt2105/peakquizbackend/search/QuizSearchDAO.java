package idatt2105.peakquizbackend.search;

import idatt2105.peakquizbackend.model.Quiz;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Class for fuzzy finding quizzes that contain a keyword
 */
@Repository
public class QuizSearchDAO {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fuzzy finds a quiz using the quiz title, description, questions text and question answers
     * 
     * @param text
     *            Keyword used to fuzzy find
     * @return List of fuzzy found quizzes
     */
    public List<Quiz> searchQuizFuzzyQuery(String text) {
        SearchSession searchSession = Search.session((Session) entityManager);

        SearchResult<Quiz> result = searchSession.search(Quiz.class).where(f -> f.match()
                .fields("title", "description", "questions.text", "questions.explanation").matching(text).fuzzy(2))
                .fetch(10);
        return result.hits();
    }
}
