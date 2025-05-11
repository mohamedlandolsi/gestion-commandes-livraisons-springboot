package itbs.mohamedlandolsi.gestioncommandeslivraisons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Ensure this matches your API base path
                .allowedOrigins("http://localhost:3000") // Your Next.js frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Added PATCH explicitly
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
