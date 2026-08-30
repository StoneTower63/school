package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Long addStudent(Student student) {
        logger.info("Was invoked method for add student");
        Student savedStudent = studentRepository.save(student);
        return savedStudent.getId();
    }

    public Student findStudent(Long id) {
        logger.info("Was invoked method for find student");
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no student with id = " + id);
                    return new IllegalArgumentException("Student not found with id: " + id);
                });
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find students by age between");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Was invoked method for get faculty by student id");
        Student student = findStudent(studentId);
        return student.getFaculty();
    }

    public Long getCountOfAllStudents() {
        logger.info("Was invoked method for get count of all students");
        Long count = studentRepository.getCountOfAllStudents();
        logger.debug("Count of all students from repository is: {}", count);
        return count;
    }

    public Double getAverageAgeOfStudents() {
        logger.info("Was invoked method for get average age of students");
        Double average = studentRepository.getAverageAgeOfStudents();
        if (average == null) {
            logger.debug("Average age from repository is null, returning 0.0");
            return 0.0;
        }
        logger.debug("Average age of students from repository is: {}", average);
        return average;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        List<Student> students = studentRepository.getLastFiveStudents();
        logger.debug("Count of last students fetched from repository is: {}", students.size());
        return students;
    }

    public List<String> getAllStudentsStartingWithA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .toList();
    }


}
