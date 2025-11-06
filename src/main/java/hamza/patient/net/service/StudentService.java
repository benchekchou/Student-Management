package hamza.patient.net.service;

import hamza.patient.net.domain.Student;
import hamza.patient.net.exceptions.NotFoundException;
import hamza.patient.net.repository.StudentRepository;
import hamza.patient.net.util.IdGenerator;
import hamza.patient.net.util.Validation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for students CRUD and lookups.
 */
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        Validation.requireNotNull(repository, "StudentRepository");
        this.repository = repository;
    }

    public Student createStudent(String name, String email) {
        Validation.requireNonBlank(name, "Nom");
        Validation.requireEmail(email);

        Student student = new Student();
        student.setId(IdGenerator.newId());
        student.setName(name.trim());
        student.setEmail(email.trim().toLowerCase());
        return repository.create(student);
    }

    public List<Student> listStudents() {
        return repository.findAll().stream()
                .sorted(Comparator
                        .comparing(Student::getName, Comparator.nullsLast(String::compareToIgnoreCase))
                        .thenComparing(Student::getId))
                .collect(Collectors.toList());
    }

    public Student getStudent(String id) {
        Validation.requireNonBlank(id, "Student ID");
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Étudiant introuvable : " + id));
    }

    public Student updateStudent(String id, String newName, String newEmail) {
        Student existing = getStudent(id);
        boolean changed = false;

        if (newName != null && !newName.trim().isEmpty()) {
            Validation.requireNonBlank(newName, "Nom");
            String sanitizedName = newName.trim();
            if (!sanitizedName.equals(existing.getName())) {
                existing.setName(sanitizedName);
                changed = true;
            }
        }

        if (newEmail != null && !newEmail.trim().isEmpty()) {
            Validation.requireEmail(newEmail);
            String sanitizedEmail = newEmail.trim().toLowerCase();
            if (!sanitizedEmail.equals(existing.getEmail())) {
                existing.setEmail(sanitizedEmail);
                changed = true;
            }
        }

        if (!changed) {
            return existing;
        }

        return repository.update(existing);
    }

    public void deleteStudent(String id) {
        Student existing = getStudent(id);
        repository.delete(existing.getId());
    }

    public List<Student> searchStudents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        String needle = query.trim().toLowerCase();
        return repository.findAll().stream()
                .filter(student -> matches(student, needle))
                .sorted(Comparator
                        .comparing(Student::getName, Comparator.nullsLast(String::compareToIgnoreCase))
                        .thenComparing(Student::getId))
                .collect(Collectors.toList());
    }

    private boolean matches(Student student, String needle) {
        if (student.getName() != null && student.getName().toLowerCase().contains(needle)) {
            return true;
        }
        return student.getEmail() != null && student.getEmail().toLowerCase().contains(needle);
    }
}
