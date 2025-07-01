package ru.mypetproject.urlshortenerapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .version("1.0.0")
                        .description("Сервис для сокращения URL и аналитики переходов")
                        .contact(new Contact()
                                .name("Техподдержка")
                                .email("support@urlshortener.com")
                                .url("https://help.urlshortener.com")));
    }
}
