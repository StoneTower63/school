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

import java.util.Collection;

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

    @Test
    void testGetFacultyInfo() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColor("Green");
        Long createdId = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Long.class);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + createdId, Faculty.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Slytherin");

        ResponseEntity<String> negativeResponse = restTemplate.getForEntity("http://localhost:" + port + "/faculty/-1", String.class);
        Assertions.assertThat(negativeResponse.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void testEditFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");
        Long createdId = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Long.class);

        faculty.setId(createdId);
        faculty.setColor("Black");
        restTemplate.put("http://localhost:" + port + "/faculty", faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + createdId, Faculty.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getColor()).isEqualTo("Black");
    }

    @Test
    void testDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");
        Long createdId = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Long.class);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + createdId);

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + createdId, String.class);
        Assertions.assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void testGetFacultiesBySearch() throws Exception {
        facultyRepository.deleteAll();

        Faculty targetFaculty = new Faculty();
        targetFaculty.setName("Gryffindor");
        targetFaculty.setColor("Red");
        restTemplate.postForObject("http://localhost:" + port + "/faculty", targetFaculty, Long.class);

        Collection<Faculty> result = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty?nameOrColor=Red",
                Collection.class
        );

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}
