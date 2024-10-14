package ru.gb.aspect.logging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(value = "application.logging.enabled", havingValue = "true")
public class LoggingAutoConfiguration {

  @Bean
  public LoggingAspect loggingAspect(LoggingProperties properties) {
    return new LoggingAspect(properties);
  }

}