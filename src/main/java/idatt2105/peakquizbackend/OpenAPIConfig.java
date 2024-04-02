package idatt2105.peakquizbackend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI defineOpenApi() {
    Server server = new Server();
    server.setUrl("http://localhost:8080");
    server.setDescription("Development");

    Contact myContact = new Contact();
    myContact.setName("Harry Linrui Xu");
    myContact.setEmail("xulr0820@hotmail.com");

    Info information = new Info()
        .title("PeakQuiz API")
        .version("1.0")
        .description("This API exposes endpoints for the PeakQuiz application.")
        .contact(myContact);
    return new OpenAPI().info(information).servers(List.of(server));
  }
}