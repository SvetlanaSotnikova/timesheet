package ru.gb.timesheet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

  @Bean
  GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

  @Bean
  SecurityFilterChain oauth2Security(HttpSecurity http) throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(it -> it
        .requestMatchers("/timesheets/**").hasRole("timesheet")
        .anyRequest().authenticated()
      )
      .oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer
        .jwt(jwtConfigurer -> {
          JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
          converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");
            List<String> roles = realmAccess.get("roles");

            return roles.stream()
              .map(SimpleGrantedAuthority::new)
              .map(it -> (GrantedAuthority) it)
              .toList();
          });
          jwtConfigurer.jwtAuthenticationConverter(converter);
        })
      )
//      .oauth2ResourceServer(oAuth2ResourceServerConfigurer -> {
//        oAuth2ResourceServerConfigurer.configure(http);
//      })
      .build();
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
