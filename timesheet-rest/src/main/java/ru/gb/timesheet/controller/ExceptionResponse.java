package ru.gb.timesheet.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Тип ответа с ошибкой")
public class ExceptionResponse {

  @Schema(description = "Текст ошибки")
  private String reason;

}
