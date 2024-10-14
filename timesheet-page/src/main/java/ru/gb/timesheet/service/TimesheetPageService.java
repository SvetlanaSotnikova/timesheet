package ru.gb.timesheet.service;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import ru.gb.timesheet.client.ProjectResponse;
import ru.gb.timesheet.client.TimesheetResponse;
import ru.gb.timesheet.controller.TimesheetPageDto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TimesheetPageService {

  private final DiscoveryClient discoveryClient;

  public TimesheetPageService(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }

  private RestClient restClient() {
    List<ServiceInstance> instances = discoveryClient.getInstances("TIMESHEET-REST");
    int instancesCount = instances.size();
    int instanceIndex = ThreadLocalRandom.current().nextInt(0, instancesCount);

    ServiceInstance instance = instances.get(instanceIndex);
    String uri = "http://" + instance.getHost() + ":" + instance.getPort();
    System.out.println("URI = " + uri);
    return RestClient.create(uri);
  }

  public List<TimesheetPageDto> findAll() {
    List<TimesheetResponse> timesheets = null;
    int attempts = 5;
    while (attempts-- > 0) {
      try {
        timesheets = restClient().get()
          .uri("/timesheets")
          .retrieve()
          .body(new ParameterizedTypeReference<List<TimesheetResponse>>() {
          });
        break;
      } catch (HttpServerErrorException e) {
        // ignore
      }
    }
    if (timesheets == null) {
      throw new RuntimeException("oops");
    }

    List<TimesheetPageDto> result = new ArrayList<>();
    for (TimesheetResponse timesheet : timesheets) {
      TimesheetPageDto timesheetPageDto = new TimesheetPageDto();
      timesheetPageDto.setId(String.valueOf(timesheet.getId()));
      timesheetPageDto.setMinutes(String.valueOf(timesheet.getMinutes()));
      timesheetPageDto.setCreatedAt(timesheet.getCreatedAt().format(DateTimeFormatter.ISO_DATE));

      ProjectResponse project = restClient().get()
        .uri("/projects/" + timesheet.getProjectId())
        .retrieve()
        .body(ProjectResponse.class);
      timesheetPageDto.setProjectName(project.getName());

      result.add(timesheetPageDto);
    }

    return result;
  }

  public Optional<TimesheetPageDto> findById(Long id) {
    try {
      TimesheetResponse timesheet = restClient().get()
        .uri("/timesheets/" + id)
        .retrieve()
        .body(TimesheetResponse.class);

      TimesheetPageDto timesheetPageDto = new TimesheetPageDto();
      timesheetPageDto.setId(String.valueOf(timesheet.getId()));
      timesheetPageDto.setMinutes(String.valueOf(timesheet.getMinutes()));
      timesheetPageDto.setCreatedAt(timesheet.getCreatedAt().format(DateTimeFormatter.ISO_DATE));

      ProjectResponse project = restClient().get()
        .uri("/projects/" + timesheet.getProjectId())
        .retrieve()
        .body(ProjectResponse.class);
      timesheetPageDto.setProjectName(project.getName());
      return Optional.of(timesheetPageDto);
    } catch (HttpClientErrorException.NotFound e) {
      return Optional.empty();
    }
  }

}
