package ac.OneBlood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class OneBloodApplication {
    //TODO: de modificat pe API-ul specific
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/**")
//                        .allowedMethods("GET","POST", "PUT", "DELETE", "PATCH")
//                        .allowedHeaders("*")
//                        .allowedOrigins("http://localhost:4200")
//                        .allowCredentials(true);
//            }
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(OneBloodApplication.class, args);
    }

}
