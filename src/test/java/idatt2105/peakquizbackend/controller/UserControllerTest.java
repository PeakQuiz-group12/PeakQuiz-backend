package idatt2105.peakquizbackend.controller;

import idatt2105.peakquizbackend.security.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest({UserController.class, SecurityConfig.class })
public class UserControllerTest {

  @TestConfiguration
  static class MapperTestConfiguration {
  }

  @Autowired
  private MockMvc mvc;

  @Test
  @WithMockUser
  public void testCreateQuiz() throws Exception {

  }
}
