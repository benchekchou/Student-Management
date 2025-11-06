package hamza.patient.net.service;

import hamza.patient.net.domain.Student;
import hamza.patient.net.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private InMemoryStudentRepository repository;
    private StudentService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryStudentRepository();
        service = new StudentService(repository);
    }

    @Test
    void createStudent_normalizesData() {
        Student created = service.createStudent("  Alice  ", "ALICE@example.com");

        assertNotNull(created.getId());
        assertEquals("Alice", created.getName());
        assertEquals("alice@example.com", created.getEmail());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void updateStudent_updatesOnlyProvidedFields() {
        Student created = service.createStudent("Bob", "bob@example.com");

        Student updated = service.updateStudent(created.getId(), "Robert", "");

        assertEquals("Robert", updated.getName());
        assertEquals("bob@example.com", updated.getEmail());
    }

    @Test
    void searchStudents_matchesOnNameOrEmail() {
        service.createStudent("Charlie", "charlie@example.com");
        service.createStudent("David", "dave@school.com");

        List<Student> results = service.searchStudents("dav");

        assertEquals(1, results.size());
        assertEquals("David", results.get(0).getName());
    }

    private static class InMemoryStudentRepository implements StudentRepository {
        private final List<Student> data = new ArrayList<>();

        @Override
        public List<Student> loadAll() {
            return List.copyOf(data);
        }

        @Override
        public void saveAll(List<Student> students) {
            data.clear();
            data.addAll(copy(students));
        }

        @Override
        public List<Student> findAll() {
            return copy(data);
        }

        @Override
        public Optional<Student> findById(String id) {
            return data.stream()
                    .filter(student -> student.getId().equals(id))
                    .findFirst()
                    .map(StudentServiceTest.InMemoryStudentRepository::duplicate);
        }

        @Override
        public Student create(Student s) {
            data.add(duplicate(s));
            return duplicate(s);
        }

        @Override
        public Student update(Student s) {
            findById(s.getId()).orElseThrow();
            data.replaceAll(student -> student.getId().equals(s.getId()) ? duplicate(s) : student);
            return duplicate(s);
        }

        @Override
        public void delete(String id) {
            data.removeIf(student -> student.getId().equals(id));
        }

        private static List<Student> copy(List<Student> students) {
            return students.stream()
                    .map(StudentServiceTest.InMemoryStudentRepository::duplicate)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }

        private static Student duplicate(Student original) {
            return new Student(original.getId(), original.getName(), original.getEmail(), original.getNotes());
        }
    }
}
