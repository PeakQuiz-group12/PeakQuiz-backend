package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.mapper.QuizMapper;
import idatt2105.peakquizbackend.search.QuizSearchDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for fuzzy finding quizzes
 */
@Service
@AllArgsConstructor
public class QuizSearchService {

    private final QuizSearchDAO quizSearchDAO;

    /**
     * Returns a set of quiz DTOs that were fuzzy found from the query of the keyword
     * 
     * @param text
     *            Keyword for fuzzy finding
     * @return Set of quiz DTOs that were fuzzy found
     */
    public Set<QuizResponseDTO> searchForQuiz(String text) {
        return quizSearchDAO.searchQuizFuzzyQuery(text).stream().map(QuizMapper.INSTANCE::toDTO)
                .collect(Collectors.toSet());
    }

}
