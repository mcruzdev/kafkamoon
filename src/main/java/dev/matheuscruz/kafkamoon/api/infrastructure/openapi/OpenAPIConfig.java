package dev.matheuscruz.kafkamoon.api.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class OpenAPIConfig {

  @Value("${kafkamoon.docs.url}")
  private String externalUrl;

  @Value("classpath:/openapi.yaml")
  private Resource openapi;

  @Bean
  public OpenAPI groupedOpenApi() throws IOException {
    return new OpenAPIV3Parser()
        .readContents(openapi.getContentAsString(StandardCharsets.UTF_8))
        .getOpenAPI();
  }
}
