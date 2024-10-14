package ru.gb.timesheet.controller;

import lombok.Data;

/**
 * Класс (DTO Data Transfer Object), который описывает параметры для шаблоново HTML-страниц.
 * Т.е. он нужен для передачи параметров внутрь thymeleaf в тех контроллерах, которые сразу отдают HTML-страницы.
 */
@Data
public class TimesheetPageDto {

  private String projectName;
  private String id;
  private String minutes;
  private String createdAt;

}
