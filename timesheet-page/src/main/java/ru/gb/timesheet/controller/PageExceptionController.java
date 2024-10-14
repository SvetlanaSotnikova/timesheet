package ru.gb.timesheet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@Controller
@ControllerAdvice(basePackageClasses = PageExceptionController.class)
//@ControllerAdvice(basePackages = "ru.gb.timesheet.page")
public class PageExceptionController {

  @GetMapping("/oops")
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String oopsPage() {
    return "oops.html";
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNoSuchElementException(NoSuchElementException e) {
    return "not-found.html";
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Exception e) {
    return "redirect:/oops";
  }

}
