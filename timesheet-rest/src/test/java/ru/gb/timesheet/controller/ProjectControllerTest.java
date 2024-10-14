package ru.gb.timesheet.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.repository.ProjectRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
class ProjectControllerTest {

  @Autowired
  ProjectRepository projectRepository;

//  @Autowired
//  WebTestClient webTestClient;

  @LocalServerPort
  private int port;
  private RestClient restClient;

  // select * from users where id = 5; // ждем 5 минут
  // select * from users; -> получаем данные порциями по 100 записей
  // Mono Flux

  @BeforeEach
  void beforeEach() {
    restClient = RestClient.create("http://localhost:" + port);
  }

  @Test
  void getByIdNotFound() {
//    try {
//      ///
//
//      fail();
//    } catch (HttpClientErrorException.BadRequest e) {
//       assertTrue(true);
//    }

    assertThrows(HttpClientErrorException.NotFound.class, () -> {
      restClient.get()
        .uri("/projects/-2")
        .retrieve()
        .toBodilessEntity();
    });
  }

  @Test
  void getByIdAllOk() {
    // given
    Project project = new Project();
    project.setName("projectName");
    Project expected = projectRepository.save(project);

//    webTestClient.get()
//      .uri("/projects/" + project.getId())
//      .exchange() // retrieve
//      .expectStatus().isOk() //   assertEquals(HttpStatus.OK, actual.getStatusCode());
//      .expectBody(Project.class)
//      .value(actual -> {
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getName(), actual.getName());
//      });
    // GET /projects/{id}
    ResponseEntity<Project> actual = restClient.get()
      .uri("/projects/" + expected.getId())
      .retrieve()
      .toEntity(Project.class);

    // assert 200 OK
    assertEquals(HttpStatus.OK, actual.getStatusCode());
    Project responseBody = actual.getBody();
    assertNotNull(responseBody);
    assertEquals(project.getId(), responseBody.getId());
    assertEquals(project.getName(), responseBody.getName());
  }

  @Test
  void testCreate() {
    // POST /projects
    Project toCreate = new Project();
    toCreate.setName("NewName");

    ResponseEntity<Project> response = restClient.post()
      .uri("/projects")
      .body(toCreate)
      .retrieve()
      .toEntity(Project.class);

    // Проверяем HTTP-ручку сервера
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Project responseBody = response.getBody();
    assertNotNull(responseBody);
    assertNotNull(responseBody.getId());
    assertEquals(responseBody.getName(), toCreate.getName());

    // Проверяем, что запись в БД есть
    assertTrue(projectRepository.existsById(responseBody.getId()));
  }

  @Test
  void testDeleteById() {
    Project toDelete = new Project();
    toDelete.setName("NewName");
    toDelete = projectRepository.save(toDelete);

    ResponseEntity<Void> response = restClient.delete()
      .uri("/projects/" + toDelete.getId())
      .retrieve()
      .toBodilessEntity(); // less
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    // Проверяем, что запись в БД НЕТ
    assertFalse(projectRepository.existsById(toDelete.getId()));
  }

}