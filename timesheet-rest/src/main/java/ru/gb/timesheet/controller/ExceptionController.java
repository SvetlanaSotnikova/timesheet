package ru.gb.timesheet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice(basePackageClasses = ExceptionController.class)
public class ExceptionController {

  //  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleException(Exception e) {
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setReason(e.getMessage());
    return ResponseEntity.internalServerError().body(exceptionResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
//    ExceptionResponse exceptionResponse = new ExceptionResponse();
//    exceptionResponse.setReason(e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

}
