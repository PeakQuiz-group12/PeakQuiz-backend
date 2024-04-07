
package idatt2105.peakquizbackend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import idatt2105.peakquizbackend.exceptions.QuizNotFoundException;
import idatt2105.peakquizbackend.mapper.GameMapper;
import idatt2105.peakquizbackend.mapper.GameMapperImpl;
import idatt2105.peakquizbackend.mapper.QuestionMapper;
import idatt2105.peakquizbackend.mapper.QuestionMapperImpl;
import idatt2105.peakquizbackend.mapper.QuizMapper;
import idatt2105.peakquizbackend.mapper.QuizMapperImpl;
import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.security.SecurityConfig;
import idatt2105.peakquizbackend.service.CategoryService;
import idatt2105.peakquizbackend.service.GameService;
import idatt2105.peakquizbackend.service.QuestionService;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.UserService;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest({ QuizController.class, SecurityConfig.class })
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

        @Bean
        public GameMapper gameMapper() {
            return new GameMapperImpl();
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

    @MockBean
    UserService userService;

    @MockBean
    private GameService gameService;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuizMapper quizMapper;

    @Autowired
    GameMapper gameMapper;

    @Test
    @WithMockUser
    public void testCreateQuiz() throws Exception {
        String categoryName1 = "History";
        String categoryName2 = "Math";
        when(categoryService.findCategoryByName(categoryName1)).thenReturn(new Category(categoryName1));
        when(categoryService.findCategoryByName(categoryName2)).thenReturn(new Category(categoryName2));
        Quiz quiz = new Quiz();
        when(quizService.saveQuiz(quiz)).thenReturn(quiz);
        String content = "{" + "  \"title\": \"username\"," + "  \"description\": \"description\","
                + "  \"playCount\": 0," + "  \"questions\": []," + "  \"categories\": [\"History\", \"Math\"]" + "}";
        mvc.perform(MockMvcRequestBuilders.post("/quizzes").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testCreateQuizWithNullCategories() throws Exception {
        Quiz quiz = new Quiz();
        when(quizService.saveQuiz(quiz)).thenReturn(quiz);
        String content = "{" + "  \"title\": \"username\"," + "  \"description\": \"description\","
                + "  \"playCount\": 0," + "  \"questions\": []" + "}";
        mvc.perform(MockMvcRequestBuilders.post("/quizzes").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testGetQuizzes() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setId(1L);

        List<Quiz> quizzes = new ArrayList<>();
        quizzes.add(quiz);

        Mockito.when(quizService.findAllQuizzes(PageRequest.of(0, 6, Sort.by("id").ascending())))
                .thenReturn(new PageImpl<>(quizzes, PageRequest.of(0, 3), 1));

        mvc.perform(get("/quizzes").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @WithMockUser
    public void testGetQuiz() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Sample Quiz");

        when(quizService.findQuizById(1L)).thenReturn(quiz);

        mvc.perform(get("/quizzes/{id}", 1L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample Quiz"));
    }

    @Test
    @WithMockUser
    public void testGetNonExistentQuiz() throws Exception {
        when(quizService.findQuizById(1L)).thenThrow(QuizNotFoundException.class);
        mvc.perform(get("/quizzes/{id}", 1L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testDeleteQuiz() throws Exception {
        mvc.perform(delete("/quizzes/{id}", 1L)).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    public void testPutQuiz() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Sample Quiz");

        when(quizService.saveQuiz(quiz)).thenReturn(quiz);
        when(quizService.findQuizById(1L)).thenReturn(quiz);

        mvc.perform(put("/quizzes/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                .content("{ \"title\": \"Updated Quiz\" }")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Quiz"));
    }

    @Test
    @WithMockUser
    public void testPutWithNonExistentQuiz() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Sample Quiz");

        when(quizService.findQuizById(1L)).thenThrow(QuizNotFoundException.class);

        mvc.perform(put("/quizzes/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                .content("{ \"title\": \"Updated Quiz\" }")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testAddingCategoriesToQuiz() throws Exception {
        String categoryName1 = "History";
        String categoryName2 = "Math";
        when(categoryService.findCategoryByName(categoryName1)).thenReturn(new Category(categoryName1));
        when(categoryService.findCategoryByName(categoryName2)).thenReturn(new Category(categoryName2));

        Quiz quiz = new Quiz();
        quiz.setId(1L);

        when(quizService.saveQuiz(quiz)).thenReturn(quiz);
        when(quizService.findQuizById(1L)).thenReturn(quiz);

        String content = "{\"categories\": [\"History\", \"Math\"]}";
        mvc.perform(put("/quizzes/{id}", 1L).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk()).andExpect(jsonPath("$.categories", hasSize(2)));
    }

    @Test
    @WithMockUser
    public void testRemovingCategoriesFromQuiz() throws Exception {
        String categoryName1 = "History";
        String categoryName2 = "Math";
        when(categoryService.findCategoryByName(categoryName1)).thenReturn(new Category(categoryName1));
        when(categoryService.findCategoryByName(categoryName2)).thenReturn(new Category(categoryName2));

        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.getCategories().add(new Category(categoryName1));
        quiz.getCategories().add(new Category(categoryName2));

        assertEquals(2, quiz.getCategories().size());

        when(quizService.saveQuiz(quiz)).thenReturn(quiz);
        when(quizService.findQuizById(1L)).thenReturn(quiz);

        String content = "{\"categories\": []}";
        mvc.perform(put("/quizzes/{id}", 1L).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk()).andExpect(jsonPath("$.categories", hasSize(0)));
    }

    @Test
    @WithMockUser
    public void testAddingQuestionsToQuiz() throws Exception {

        Quiz quiz = new Quiz();
        quiz.setId(1L);

        assertEquals(0, quiz.getQuestions().size());

        when(quizService.saveQuiz(quiz)).thenReturn(quiz);
        when(quizService.findQuizById(1L)).thenReturn(quiz);

        String content = "{" + "\"questions\": [" + "{" + "\"id\": null," + "\"text\": \"dddd\","
                + "\"media\": \"https://www.zelda.com/breath-of-the-wild/assets/icons/BOTW-Share_icon.jpg\","
                + "\"questionType\": \"MULTIPLE_CHOICE\"," + "\"difficulty\": 5," + "\"explanation\": \"Yes\","
                + "\"answers\": [" + "{" + "\"answer\": \"newanswer\"," + "\"isAnswer\": false" + "}" + "],"
                + "\"quizId\": 1" + "}]" + "}";
        mvc.perform(put("/quizzes/{id}", 1L).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk()).andExpect(jsonPath("$.questions", hasSize(1)));
    }

    @Test
    @WithMockUser
    public void testRemovingQuestionsFromQuiz() throws Exception {

        Quiz quiz = new Quiz();
        quiz.setId(1L);
        Question question1 = new Question();
        question1.setText("lol");
        quiz.addQuestion(question1);
        quiz.addQuestion(new Question());

        assertEquals(2, quiz.getQuestions().size());

        when(quizService.saveQuiz(quiz)).thenReturn(quiz);
        when(quizService.findQuizById(1L)).thenReturn(quiz);

        String content = "{\"questions\": []}";
        mvc.perform(put("/quizzes/{id}", 1L).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk()).andExpect(jsonPath("$.questions", hasSize(0)));
    }
}
