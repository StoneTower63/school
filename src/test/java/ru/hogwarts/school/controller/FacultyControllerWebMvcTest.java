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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateFaculty() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Gold");

        Mockito.when(facultyService.addFaculty(Mockito.any(Faculty.class))).thenReturn(facultyId);

        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(facultyId)));
    }

    @Test
    void testGetFacultyInfo() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName("Slytherin");
        faculty.setColor("Green");

        Mockito.when(facultyService.findFaculty(facultyId)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/" + facultyId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(facultyId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Slytherin"));

        Mockito.when(facultyService.findFaculty(-1L)).thenThrow(new IllegalArgumentException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/-1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testEditFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Hufflepuff");
        faculty.setColor("Black");

        Mockito.when(facultyService.editFaculty(Mockito.any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("Black"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        Long facultyId = 1L;

        Mockito.doNothing().when(facultyService).deleteFaculty(facultyId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/" + facultyId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetFacultiesBySearch() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        List<Faculty> faculties = List.of(faculty);

        Mockito.when(facultyService.searchByNameOrColor("Red")).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty")
                        .param("nameOrColor", "Red"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].color").value("Red"));
    }
}
