package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("{id}")
    public Student getStudentInfo(@PathVariable Long id) {
        return studentService.findStudent(id);
    }

    @PutMapping
    public Student editStudent(@RequestBody Student student) {
        return studentService.editStudent(student);
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping
    public Collection<Student> getStudentsByAgeRange(
            @RequestParam int min,
            @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFacultyOfStudent(@PathVariable Long id) {
        return studentService.getFacultyByStudentId(id);
    }

    @GetMapping("/count")
    public Long getCountOfAllStudents() {
        return studentService.getCountOfAllStudents();
    }

    @GetMapping("/average-age")
    public Double getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }

    @GetMapping("/last-five")
    public Collection<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-starting-with-a")
    public java.util.List<String> getAllStudentsStartingWithA() {
        return studentService.getAllStudentsStartingWithA();
    }

    @GetMapping("/average-age-stream")
    public double getAverageAgeOfStudentsWithStreams() {
        return studentService.getAverageAgeOfStudentsWithStreams();
    }

    @GetMapping("/parallel-sum")
    public long getParallelSum() {
        return studentService.getParallelSum();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @GetMapping("/students/print-parallel")
    public void printStudentsParallel() {
        studentService.printStudentsParallel();
    }

}
