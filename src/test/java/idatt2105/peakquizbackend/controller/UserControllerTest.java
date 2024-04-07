package idatt2105.peakquizbackend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import idatt2105.peakquizbackend.mapper.*;
import idatt2105.peakquizbackend.model.Game;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.Tag;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.security.SecurityConfig;
import idatt2105.peakquizbackend.service.*;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest({ UserController.class, SecurityConfig.class })
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    GameService gameService;
    @MockBean
    TagService tagService;
    @MockBean
    private QuizService quizService;

    @TestConfiguration
    static class MapperTestConfiguration {
        @Bean
        public TagMapper tagMapper() {
            return new TagMapperImpl();
        }

        @Bean
        public QuestionMapper questionMapper() {
            return new QuestionMapperImpl();
        }

        @Bean
        public GameMapper gameMapper() {
            return new GameMapperImpl();
        }
    }

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    GameMapper gameMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    public void testGetGames() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");

        user.setId(1L);
        Quiz quiz = new Quiz();
        quiz.setId(1L);

        userService.saveUser(user);
        quizService.saveQuiz(quiz);
        Game game = new Game(2, (byte) 2, "Nice game!", user, quiz);

        user.getGames().add(game);
        userService.saveUser(user);
        quizService.saveQuiz(quiz);

        when(userService.findUserByUsername(user.getUsername())).thenReturn(user);

        mvc.perform(get("/users/test/games").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("test"))
                .andExpect(jsonPath("$[0].quizId").value(quiz.getId()));
    }

    @Test
    @WithMockUser
    public void testGetUsers() throws Exception {
        List<User> users = List.of(new User());
        when(userService.findAllUsers()).thenReturn(users);

        mvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser
    public void getTags() throws Exception {
        Tag tag = new Tag();
        Set<Tag> tags = Set.of(tag);
        User user = new User();
        user.setUsername("test");
        user.setTags(tags);
        tag.setUser(user);

        when(userService.findUserByUsername("test")).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/users/test/tags")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }
}
