package dev.matheuscruz.kafkamoon.api.infrastructure.openapi;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Value("${kafkamoon.docs.url}")
  private String externalUrl;

  @Bean
  public OpenAPI groupedOpenApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Kafkamoon API")
                .description("Kafka management API for hiring test")
                .version("0.1.0"))
        .externalDocs(
            new ExternalDocumentation().url(externalUrl).description("Kafkamoon Documentation"));
  }
}
