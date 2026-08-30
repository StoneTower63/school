package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Long addFaculty(Faculty faculty) {
        logger.info("Was invoked method for add faculty");
        Faculty savedFaculty = facultyRepository.save(faculty);
        return savedFaculty.getId();
    }

    public Faculty findFaculty(Long id) {
        logger.info("Was invoked method for find faculty");
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id = " + id);
                    return new IllegalArgumentException("Faculty not found with id: " + id);
                });
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> searchByNameOrColor(String keyword) {
        logger.info("Was invoked method for search faculties by name or color");
        logger.debug("Search keyword used in repository is: {}", keyword);
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(keyword, keyword);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Was invoked method for get students by faculty id");
        Faculty faculty = findFaculty(facultyId);
        return faculty.getStudents();
    }

    public String getLongestFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }

}
