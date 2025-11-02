package hamza.patient.net.repository.file;

import hamza.patient.net.domain.Student;
import hamza.patient.net.repository.StudentRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileStudentRepository implements StudentRepository {
    private final Path path;
    private final List<Student> cache = new ArrayList<>();
    private final TxtSerializer serializer;

    public FileStudentRepository(Path path, TxtSerializer serializer) {
        this.path = path;
        this.serializer = serializer;
        loadAll();
    }

    @Override
    public List<Student> loadAll() {
        return List.of();
    }

    @Override
    public void saveAll(List<Student> students) {

    }

    @Override
    public List<Student> findAll() {
        return List.of();
    }

    @Override
    public Optional<Student> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Student create(Student s) {
        return null;
    }

    @Override
    public Student update(Student s) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
