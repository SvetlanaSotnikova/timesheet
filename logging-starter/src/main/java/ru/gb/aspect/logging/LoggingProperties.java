package ru.gb.aspect.logging;

import lombok.Data;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("application.logging")
public class LoggingProperties {

  private Level level = Level.DEBUG;

  private boolean printArgs = false;

  // enum, string, (int, long), boolean,
  // List<...>
  // Map<..., ...>
  // Map<String, List<String>> mapping;
  // Any class

//  private Output output;
//
//  @Data
//  public static class Output {
//    private String type;
//    private Level level;
//    private boolean enabled = true;
//  }

}
