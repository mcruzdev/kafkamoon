package dev.matheuscruz.kafkamoon.api.infrastructure.security;

import static dev.matheuscruz.kafkamoon.api.infrastructure.security.JWTConverter.ROLE_PREFIX;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  public static final String ROLE_READER = "READER";
  public static final String ROLE_WRITER = "WRITER";

  @Bean
  @Profile({"k8s", "it"})
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(
            oauth2 -> {
              oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new JWTConverter()));
            });
    return http.build();
  }

  @Bean
  @Profile({"!k8s", "!it"})
  public SecurityFilterChain disableSecurity(HttpSecurity http) throws Exception {
    http.anonymous(
            httpSecurityAnonymousConfigurer -> {
              httpSecurityAnonymousConfigurer.authorities(
                  List.of(
                      new SimpleGrantedAuthority(
                          ROLE_PREFIX.concat(SecurityConfiguration.ROLE_WRITER)),
                      new SimpleGrantedAuthority(
                          ROLE_PREFIX.concat(SecurityConfiguration.ROLE_READER))));
            })
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }
}
