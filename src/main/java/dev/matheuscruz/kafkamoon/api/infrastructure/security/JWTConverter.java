package dev.matheuscruz.kafkamoon.api.infrastructure.security;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class JWTConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private static final String REALM_ACCESS = "realm_access";
  private static final String ROLES = "roles";
  private static final String ROLE_PREFIX = "ROLE_";

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {

    Map<String, Collection<String>> realm = jwt.getClaim(REALM_ACCESS);
    Collection<String> roles = realm.get(ROLES);
    List<SimpleGrantedAuthority> grantedAuthorities =
        roles.stream()
            .map(
                role ->
                    new SimpleGrantedAuthority(ROLE_PREFIX.concat(role).toUpperCase(Locale.ROOT)))
            .toList();

    return new JwtAuthenticationToken(jwt, grantedAuthorities);
  }
}
