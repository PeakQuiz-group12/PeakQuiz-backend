
package idatt2105.peakquizbackend.controller;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.security.SecurityConfig;
import idatt2105.peakquizbackend.service.QuizService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest({ QuizControllerTest.class, SecurityConfig.class })
public class QuizControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private QuizService quizService;

  @Test
  public void testCreateQuiz() {
    Quiz quiz = new Quiz();

    when(quizService.saveQuiz(quiz)).thenReturn(quiz);

    try {
      mvc
          .perform(
              MockMvcRequestBuilders
                  .post("/quizzes/")
                  .accept(MediaType.APPLICATION_JSON)
                  .content("{" +
                      "  \"title\": \"Test Quiz\"," +
                      "  \"description\": \"This is a test quiz\"," +
                      "  \"image\": \"base64-encoded-image\"," +
                      "  \"createdON\": \"2022-04-05T10:00:00Z\"," +
                      "  \"playCount\": 0," +
                      "  \"questionIds\": [1, 2, 3]," +
                      "  \"categories\": [" +
                      "    {" +
                      "      \"id\": 1," +
                      "      \"name\": \"Category 1\"" +
                      "    }," +
                      "    {" +
                      "      \"id\": 2," +
                      "      \"name\": \"Category 2\"" +
                      "    }" +
                      "  ]" +
                      "}"
                  )
          )
          .andExpect(status().isCreated());
    } catch (Exception e) {
      fail();
    }
  }

}
