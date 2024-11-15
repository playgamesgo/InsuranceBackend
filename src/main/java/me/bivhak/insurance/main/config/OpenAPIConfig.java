package me.bivhak.insurance.main.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for setting up OpenAPI documentation.
 */
@Configuration
public class OpenAPIConfig {
    /**
     * The URL of the production server.
     */
    @Value("${backend.openapi.prod-url}")
    private String prodUrl;

    /**
     * Bean definition for OpenAPI.
     *
     * @return an OpenAPI instance configured with server and API information
     */
    @Bean
    public OpenAPI myOpenAPI() {
        // Define the production server
        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        // Define the API information
        Info info = new Info()
                .title("Insurance API")
                .version("v1");

        // Return the OpenAPI instance
        return new OpenAPI().info(info).servers(List.of(prodServer));
    }
}
