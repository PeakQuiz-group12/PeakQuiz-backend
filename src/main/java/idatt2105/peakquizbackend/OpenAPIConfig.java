package idatt2105.peakquizbackend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining OpenAPI documentation settings.
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Defines the OpenAPI bean for Swagger documentation.
     *
     * @return An instance of OpenAPI with configured settings.
     */
    @Bean
    public OpenAPI defineOpenApi() {
        // Configure server information
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        // Configure contact information
        Contact myContact = new Contact();
        myContact.setName("Harry Linrui Xu");
        myContact.setEmail("xulr0820@hotmail.com");

        // Configure general information about the API
        Info information = new Info().title("PeakQuiz API").version("1.0")
                .description("This API exposes endpoints for the PeakQuiz application.").contact(myContact);

        // Return the configured OpenAPI object
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
