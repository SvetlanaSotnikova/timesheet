package ru.gb.timesheet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;

import java.util.function.Consumer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

//  @Autowired
//  private ClientRegistrationRepository clientRegistrationRepository;

  @Bean
  ClientRegistrationRepository clientRegistrationRepository() {
    return new InMemoryClientRegistrationRepository(
      ClientRegistration.withRegistrationId("keycloak")
        .clientId("unsafe_client")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .redirectUri("http://localhost:3333/oauth2/authorization/keycloak")
        .authorizationUri("http://localhost:8080/realms/master/protocol/openid-connect/auth")
        .tokenUri("/tokenadsafdf")
        .build()
    );
  }

  @Bean
  GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

  @Bean
  SecurityFilterChain noSecurity(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(it -> it.anyRequest().authenticated())
      .oauth2Login(oauth2 -> oauth2
        .authorizationEndpoint(authorization -> authorization
          .authorizationRequestResolver(
            authorizationRequestResolver(clientRegistrationRepository)
          )
        )
      )
      .build();
  }

  private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
    ClientRegistrationRepository clientRegistrationRepository) {

    DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
      new DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository, "/oauth2/authorization");
    authorizationRequestResolver.setAuthorizationRequestCustomizer(
      authorizationRequestCustomizer());

    return authorizationRequestResolver;
  }

  private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
    return customizer -> customizer
      .additionalParameters(params -> params.put("prompt", "consent"));
  }

  //  @Bean
//  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    return http
//      .authorizeHttpRequests(requests -> requests
////          .requestMatchers("/projects/**").hasAuthority(Role.ADMIN.getName())
////        .requestMatchers("/projects/**").hasRole("admin") // MY_ROLE_PREFIX_admin
//          .requestMatchers("/timesheets/**").hasAnyAuthority(Role.USER.getName())
//          .anyRequest().authenticated()
//      )
//      .formLogin(Customizer.withDefaults())
//      .build();
//  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
