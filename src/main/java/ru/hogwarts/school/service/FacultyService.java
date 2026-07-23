package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.*;

@Service
public class FacultyService {

    private final Map<Long, Faculty> faculties = new HashMap<>();
    private Long count = 1L;

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(count);
        faculties.put(faculty.getId(), faculty);
        count++;
        return faculty;
    }

    public Faculty findFaculty(Long id) {
        if (!faculties.containsKey(id)) {
            throw new IllegalArgumentException("Faculty not found");
        }
        return faculties.get(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            throw new IllegalArgumentException("Faculty not found");
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public void deleteFaculty(Long id) {
        if (!faculties.containsKey(id)) {
            throw new IllegalArgumentException("Faculty not found");
        }
        faculties.remove(id);
    }

    public Collection<Faculty> findByColor(String color) {
        List<Faculty> result = new ArrayList<>();
        for (Faculty faculty : faculties.values()) {
            if (faculty.getColor().equalsIgnoreCase(color)) {
                result.add(faculty);
            }
        }
        return result;
    }
}
