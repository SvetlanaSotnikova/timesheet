package ru.gb.timesheet.client;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TimesheetResponse {

  private Long id;
  private Long projectId;
  private Integer minutes;
  private LocalDate createdAt;

}