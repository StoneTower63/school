package ru.hogwarts.school.controller;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setName("Hary Potter");
        student.setAge(11);

        ResponseEntity<Long> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                Long.class
        );

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isGreaterThan(0L);

    }

    @Test
    void testGetStudentInfo() throws Exception {
        Student student = new Student();
        student.setName("Ron Weasley");
        student.setAge(11);

        Long createdId = restTemplate.postForObject(
                "http://localhost:" + port + "/student",
                student,
                Long.class
        );

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + createdId,
                Student.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(createdId);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Ron Weasley");

        ResponseEntity<String> negativeResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/-1",
                String.class
        );

        Assertions.assertThat(negativeResponse.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void testEditStudent() throws Exception {
        Student student = new Student();
        student.setName("Hermione Granger");
        student.setAge(11);
        Long createdId = restTemplate.postForObject("http://localhost:" + port + "/student", student, Long.class);

        student.setId(createdId);
        student.setName("Hermione Weasley");
        restTemplate.put("http://localhost:" + port + "/student", student);

        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/student/" + createdId, Student.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Hermione Weasley");
    }

    @Test
    void testDeleteStudent() throws Exception {
        Student student = new Student();
        student.setName("Draco Malfoy");
        student.setAge(11);
        Long createdId = restTemplate.postForObject("http://localhost:" + port + "/student", student, Long.class);

        restTemplate.delete("http://localhost:" + port + "/student/" + createdId);

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/student/" + createdId, String.class);
        Assertions.assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void testGetStudentsByAgeRange() throws Exception {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        Student youngStudent = new Student();
        youngStudent.setName("Harry J.");
        youngStudent.setAge(10);
        restTemplate.postForObject("http://localhost:" + port + "/student", youngStudent, Long.class);

        Student oldStudent = new Student();
        oldStudent.setName("Albus D.");
        oldStudent.setAge(15);
        restTemplate.postForObject("http://localhost:" + port + "/student", oldStudent, Long.class);

        Collection<Student> result = restTemplate.getForObject(
                "http://localhost:" + port + "/student?min=9&max=12",
                Collection.class
        );

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}
