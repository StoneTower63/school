package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Long addFaculty(Faculty faculty) {
        Faculty savedFaculty = facultyRepository.save(faculty);
        return savedFaculty.getId();
    }

    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found with id: " + id));

    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> searchByNameOrColor(String keyword) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(keyword, keyword);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        Faculty faculty = findFaculty(facultyId);
        return faculty.getStudents();
    }
}
