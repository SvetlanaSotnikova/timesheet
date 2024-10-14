package ru.gb.timesheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@SpringBootApplication
public class TimesheetPageApplication {

  // TDD - Test Driven Development (Разработка через тестирование)

  public static void main(String[] args) {
    SpringApplication.run(TimesheetPageApplication.class, args);
  }

}
