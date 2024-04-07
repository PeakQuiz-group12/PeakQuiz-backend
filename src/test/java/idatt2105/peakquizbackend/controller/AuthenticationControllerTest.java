package idatt2105.peakquizbackend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import idatt2105.peakquizbackend.exceptions.UserAlreadyExistsException;
import idatt2105.peakquizbackend.exceptions.UserNotFoundException;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.security.SecurityConfig;
import idatt2105.peakquizbackend.service.AuthService;
import idatt2105.peakquizbackend.service.UserService;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest({ AuthenticationController.class, SecurityConfig.class })
public class AuthenticationControllerTest {
    @TestConfiguration
    static class MapperTestConfiguration {
        @Bean
        BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private JWTVerifier jwtVerifier;

    @MockBean
    private DecodedJWT decodedJWT;

    @Test
    public void testRegisterUser() throws Exception {
        String email = "test@test.com";
        when(authService.isEmailValid(email)).thenReturn(true);
        String password = "Aa11111!";
        when(authService.isPasswordStrong(password)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders.post("/register?username=test"
                + "&password="
                + password
                + "&mail="
                + email)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserWithWeakPassword() throws Exception {
        String email = "test@test.com";
        when(authService.isEmailValid(email)).thenReturn(true);
        String password = "123";
        when(authService.isPasswordStrong(password)).thenReturn(false);
        mvc.perform(MockMvcRequestBuilders.post("/register?username=test"
                + "&password="
                + password
                + "&mail="
                + email)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterWithInvalidEmail() throws Exception {
        String email = "testtest.com";
        when(authService.isEmailValid(email)).thenReturn(false);
        mvc.perform(MockMvcRequestBuilders.post("/register?username=test"
                + "&password=12345AA!"
                + "&mail="
                + email)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
    @Test
    public void testRegisterDuplicateUser() throws Exception {
        when(userService.findUserByUsername("test")).thenThrow(UserAlreadyExistsException.class);
        mvc.perform(MockMvcRequestBuilders.post("/register?username=test&password=1BVdffdfdffdf!&mail=test@test.com")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginWithValidCredentials() throws Exception {
        User user = new User();
        user.setUsername("test");
        String password = "Aa12345!";
        user.setPassword(password);
        when(userService.findUserByUsername(user.getUsername())).thenReturn(user);

        when(authService.matches(password, password)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.post("/login?username=test"
                + "&password=" + password)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testLoginWithWrongUsername() throws Exception {
        when(userService.findUserByUsername("test")).thenThrow(UserNotFoundException.class);
        mvc.perform(MockMvcRequestBuilders.post("/login?username=test&password=1BVdffdfdffdf!")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testLoginWithWrongPassword() throws Exception {
        User user = new User();
        String username = "test";
        String password = "Aa12345!";
        user.setUsername(username);
        user.setPassword(password);

        when(userService.findUserByUsername(username)).thenReturn(user);
        mvc.perform(MockMvcRequestBuilders.post("/login?username=test&password=1BVdffdfdffdf!")
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPostInvalidRefreshToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/refreshToken?refreshToken=token").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }
    @Test
    public void testPostValidRefreshToken() throws Exception {
        Algorithm algorithm = Algorithm.HMAC512("testsecrettestsecrettestsecrettestsecrettestsecret");
        String refreshToken = JWT.create()
            .withSubject("test")
            .withExpiresAt(new Date(System.currentTimeMillis()+ 5*60*1000))
            .sign(algorithm);

        when(jwtVerifier.verify(refreshToken)).thenReturn(decodedJWT);
        mvc.perform(MockMvcRequestBuilders.post("/refreshToken?refreshToken=" + refreshToken).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}