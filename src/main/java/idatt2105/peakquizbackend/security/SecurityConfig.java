package idatt2105.peakquizbackend.security;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
        .headers((headers) ->
            headers
                .frameOptions(FrameOptionsConfig::disable)
        )
    //.cors().and()
      .authorizeHttpRequests(
              authorize -> {
                authorize.requestMatchers(toH2Console()).permitAll();
                authorize
                    .requestMatchers("/console/**", "/login", "/register", "/refreshToken", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs/", "swagger-ui.html", "/webjars/**, /forgotPassword")
                    .permitAll()
                    .anyRequest()
                    .authenticated();
              }
      )
      .sessionManagement(
              manager -> manager
                      .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
