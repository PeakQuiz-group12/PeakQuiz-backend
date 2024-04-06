
package idatt2105.peakquizbackend.controller;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import idatt2105.peakquizbackend.mapper.QuestionMapper;
import idatt2105.peakquizbackend.mapper.QuestionMapperImpl;
import idatt2105.peakquizbackend.mapper.QuizMapper;
import idatt2105.peakquizbackend.mapper.QuizMapperImpl;
import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.security.SecurityConfig;
import idatt2105.peakquizbackend.service.CategoryService;
import idatt2105.peakquizbackend.service.QuestionService;
import idatt2105.peakquizbackend.service.QuizService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest({ QuizController.class, SecurityConfig.class})
public class QuizControllerTest {

  @TestConfiguration
  static class MapperTestConfiguration {
    @Bean
    public QuestionMapper questionMapper() {
      return new QuestionMapperImpl();
    }
    @Bean
    public QuizMapper quizMapper() {
      return new QuizMapperImpl();
    }
  }
  @Autowired
  private MockMvc mvc;

  @MockBean
  private QuizService quizService;

  @MockBean
  private CategoryService categoryService;

  @MockBean
  QuestionService questionService;

  @Autowired
  QuestionMapper questionMapper;

  @Autowired
  QuizMapper quizMapper;

  @Test
  @WithMockUser
  public void testCreateQuiz() throws Exception {
    String categoryName1 = "History";
    String categoryName2 = "Math";
    when(categoryService.findCategoryByName(categoryName1)).thenReturn(new Category(categoryName1));
    when(categoryService.findCategoryByName(categoryName2)).thenReturn(new Category(categoryName2));
    Quiz quiz = new Quiz();
    when(quizService.saveQuiz(quiz)).thenReturn(quiz);
    String content =
        "{" +
        "  \"title\": \"username\"," +
        "  \"description\": \"description\"," +
        "  \"playCount\": 0," +
        "  \"questions\": []," +
        "  \"categories\": [\"History\", \"Math\"]" +
        "}";
    mvc.perform(
            MockMvcRequestBuilders
                .post("/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
        )
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  public void testCreateQuizWithNullCategories() throws Exception {
    Quiz quiz = new Quiz();
    when(quizService.saveQuiz(quiz)).thenReturn(quiz);
    String content =
        "{" +
            "  \"title\": \"username\"," +
            "  \"description\": \"description\"," +
            "  \"playCount\": 0," +
            "  \"questions\": []" +
            "}";
    mvc.perform(
            MockMvcRequestBuilders
                .post("/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  public void testGetQuizzes() throws Exception {
    Quiz quiz = new Quiz();
    quiz.setId(1L);

    List<Quiz> quizzes = new ArrayList<>();
    quizzes.add(quiz);

    Mockito.when(quizService.findAllQuizzes(PageRequest.of(0,6, Sort.by("id").ascending()))).thenReturn(new PageImpl<>(quizzes,
        PageRequest.of(0,3), 1));

    mvc.perform(MockMvcRequestBuilders
            .get("/quizzes")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  //Test get spesifikk
  //Test put med adde og remove categories
  //Test put med adde og remove quetsions
  //Test delete
}
