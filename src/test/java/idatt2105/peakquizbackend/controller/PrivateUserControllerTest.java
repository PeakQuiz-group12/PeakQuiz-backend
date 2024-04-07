package idatt2105.peakquizbackend.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import idatt2105.peakquizbackend.mapper.UserMapper;
import idatt2105.peakquizbackend.mapper.UserMapperImpl;
import idatt2105.peakquizbackend.security.SecurityConfig;
import idatt2105.peakquizbackend.service.AuthService;
import idatt2105.peakquizbackend.service.CollaborationService;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest({ PrivateUserControllerTest.class, SecurityConfig.class })
public class PrivateUserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private CollaborationService collaborationService;

    @MockBean
    private QuizService quizService;

    @Autowired
    private UserMapper userMapper;

    @TestConfiguration
    static class MapperTestConfiguration {
        @Bean
        public UserMapper userMapper() {
            return new UserMapperImpl();
        }
    }

    @Test
    public void testGetMeUnauthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users/me").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateMeUnauthenticated() throws Exception {

        String content = "{password=123}";
        mvc.perform(MockMvcRequestBuilders.put("/users/me").content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }
}
