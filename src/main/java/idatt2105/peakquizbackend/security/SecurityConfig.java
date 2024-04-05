package idatt2105.peakquizbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

  // inject SecurityFilterChain and tell that all requests are authenticated
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      //.cors().and()
      .authorizeHttpRequests(
              authorize -> authorize
                      .requestMatchers("/login", "/register", "/refreshToken", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs/", "swagger-ui.html", "/webjars/**")
                      .permitAll()
                      .anyRequest()
                      .authenticated()
      )
      .sessionManagement(
              manager -> manager
                      .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
