package hamza.patient.net.repository.file;

import hamza.patient.net.domain.CourseNote;
import hamza.patient.net.domain.Student;
import hamza.patient.net.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileStudentRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void createUpdateDeleteRoundTrip() throws IOException {
        Path dataFile = tempDir.resolve("students.txt");
        FileStudentRepository repository = new FileStudentRepository(dataFile, new TxtSerializer());

        Student student = new Student("id-1", "Alice", "alice@example.com", List.of(new CourseNote("Math", 15)));
        repository.create(student);

        List<Student> stored = repository.findAll();
        assertEquals(1, stored.size());
        assertEquals("Alice", stored.get(0).getName());

        student.setEmail("new@example.com");
        repository.update(student);
        Student reloaded = repository.findById("id-1").orElseThrow();
        assertEquals("new@example.com", reloaded.getEmail());

        repository.delete("id-1");
        assertTrue(repository.findAll().isEmpty());

        String fileContent = Files.readString(dataFile);
        assertTrue(fileContent.isEmpty());

        assertThrows(NotFoundException.class, () -> repository.delete("unknown"));
    }
}
