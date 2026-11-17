package ie.universityofgalway.group11.expensestracker.budgeting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global web configuration for the backend API.
 * It enables CORS so the frontend running on the local development server can call the Spring application.
 */
@Configuration
public class WebConfig {

    /**
     * Array of allowed origins for CORS configuration.
     */
    @Value("${app.allowed.origins}")
    private String[] allowedOrigins;

    /**
     * Configures cross-origin access for the React frontend development environment.
     *
     * @return a Spring MVC CORS configuration bean
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}