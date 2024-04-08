package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.mapper.QuizMapper;
import idatt2105.peakquizbackend.search.QuizSearchDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuizSearchService {

  private final QuizSearchDAO quizSearchDAO;

  public Set<QuizResponseDTO> searchForQuiz(String text) {
    return quizSearchDAO.searchQuizByTitleFuzzyQuery(text).stream()
            .map(QuizMapper.INSTANCE::toDTO).collect(Collectors.toSet());
  }


}
