package ru.gb.timesheet.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class API {

  @Retention(RetentionPolicy.RUNTIME)
  @ApiResponse(description = "Проект не найден", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
  public @interface NotFoundResponse {

  }

}
