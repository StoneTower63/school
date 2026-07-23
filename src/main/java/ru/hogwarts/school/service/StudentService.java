package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private Long count = 1L;

    public Student addStudent(Student student) {
        student.setId(count);
        students.put(student.getId(), student);
        count++;
        return student;
    }

    public Student findStudent(Long id) {
        if (!students.containsKey(id)) {
            throw new IllegalArgumentException("Student not found");
        }
        return students.get(id);
    }

    public Student editStudent(Student student) {
        if (!students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student not found");
        }
        students.put(student.getId(), student);
        return student;
    }

    public void deleteStudent(Long id) {
        if (!students.containsKey(id)) {
            throw new IllegalArgumentException("Student not found");
        }
        students.remove(id);
    }

    public Collection<Student> findByAge(int age) {
        List<Student> result = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getAge() == age) {
                result.add(student);
            }
        }
        return result;
    }

}
