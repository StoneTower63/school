package ru.hogwarts.school.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatedStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student();
        student.setName("Harry Potter");
        student.setAge(11);

        Mockito.when(studentService.addStudent(Mockito.any(Student.class))).thenReturn(studentId);
        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(studentId)));
    }

    @Test
    void testGetStudentInfo() throws Exception {
        Long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        student.setName("Ron Weasley");
        student.setAge(11);

        Mockito.when(studentService.findStudent(studentId)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/" + studentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(studentId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ron Weasley"));

        Mockito.when(studentService.findStudent(-1L)).thenThrow(new IllegalArgumentException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/-1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testEditStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Hermione Weasley");
        student.setAge(11);

        Mockito.when(studentService.editStudent(Mockito.any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Hermione Weasley"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        Long studentId = 1L;

        Mockito.doNothing().when(studentService).deleteStudent(studentId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/" + studentId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetStudentsByAgeRange() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry J.");
        student.setAge(10);

        List<Student> students = List.of(student);

        Mockito.when(studentService.findByAgeBetween(9, 12)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/student")
                        .param("min", "9")
                        .param("max", "12"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Harry J."));
    }

}
