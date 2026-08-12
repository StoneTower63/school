package ru.hogwarts.school.controller;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(restTemplate).isNotNull();
    }

    @Test
    void testCreatedFaculty() throws Exception {
        facultyRepository.deleteAll();

        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Gold");

        ResponseEntity<Long> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty,
                Long.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isGreaterThan(0L);
    }
}
