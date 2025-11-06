package hamza.patient.net.service;

import hamza.patient.net.domain.Student;
import hamza.patient.net.repository.StudentRepository;
import hamza.patient.net.service.strategy.SimpleAverageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import static org.junit.jupiter.api.Assertions.*;

class CalculationServiceTest {

    private final InMemoryRepository repository = new InMemoryRepository();
    private CalculationService service;

    @BeforeEach
    void setUp() {
        repository.clear();
        service = new CalculationService(repository, new SimpleAverageStrategy());
    }

    @Test
    void computeAverage_returnsNullWhenNoCourses() {
        Student student = new Student("1", "Test", "test@example.com");
        repository.create(student);

        assertNull(service.computeAverage("1"));
    }

    @Test
    void bestStudentAndFailingStudentsComputed() {
        Student alice = new Student("a", "Alice", "a@example.com");
        alice.addOrUpdateNote("Math", 18);
        alice.addOrUpdateNote("Physics", 16);
        repository.create(alice);

        Student bob = new Student("b", "Bob", "b@example.com");
        bob.addOrUpdateNote("Math", 8);
        repository.create(bob);

        Optional<Student> best = service.getBestStudent();
        assertTrue(best.isPresent());
        assertEquals("Alice", best.get().getName());

        List<Student> failing = service.getFailingStudents();
        assertEquals(1, failing.size());
        assertEquals("Bob", failing.get(0).getName());

        OptionalDouble globalAverage = service.computeGlobalAverage();
        assertTrue(globalAverage.isPresent());
        assertTrue(globalAverage.getAsDouble() > 0);
    }

    private static class InMemoryRepository implements StudentRepository {
        private final List<Student> data = new ArrayList<>();

        void clear() {
            data.clear();
        }

        @Override
        public List<Student> loadAll() {
            return findAll();
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
                    .map(InMemoryRepository::duplicate);
        }

        @Override
        public Student create(Student s) {
            data.add(duplicate(s));
            return duplicate(s);
        }

        @Override
        public Student update(Student s) {
            data.replaceAll(student -> student.getId().equals(s.getId()) ? duplicate(s) : student);
            return duplicate(s);
        }

        @Override
        public void delete(String id) {
            data.removeIf(student -> student.getId().equals(id));
        }

        private static List<Student> copy(List<Student> students) {
            return students.stream()
                    .map(InMemoryRepository::duplicate)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }

        private static Student duplicate(Student original) {
            return new Student(original.getId(), original.getName(), original.getEmail(), original.getNotes());
        }
    }
}
