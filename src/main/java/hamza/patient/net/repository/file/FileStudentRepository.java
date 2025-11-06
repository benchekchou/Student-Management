package hamza.patient.net.repository.file;

import hamza.patient.net.domain.CourseNote;
import hamza.patient.net.domain.Student;
import hamza.patient.net.exceptions.NotFoundException;
import hamza.patient.net.exceptions.PersistenceException;
import hamza.patient.net.exceptions.ValidationException;
import hamza.patient.net.repository.StudentRepository;
import hamza.patient.net.util.Validation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * File-backed implementation with an in-memory cache.
 */
public class FileStudentRepository implements StudentRepository {
    private final Path path;
    private final TxtSerializer serializer;
    private final List<Student> cache = new ArrayList<>();

    public FileStudentRepository(Path path, TxtSerializer serializer) {
        this.path = path;
        this.serializer = serializer;
        loadAll();
    }

    @Override
    public synchronized List<Student> loadAll() {
        try {
            ensureFileExists();
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<Student> loaded = serializer.parse(lines);
            cache.clear();
            cache.addAll(copyStudents(loaded));
            return copyStudents(cache);
        } catch (IOException e) {
            throw new PersistenceException("Erreur lors de la lecture du fichier " + path, e);
        }
    }

    @Override
    public synchronized void saveAll(List<Student> students) {
        Validation.requireNotNull(students, "Liste d'étudiants");
        List<Student> snapshot = copyStudents(students);
        writeToFile(snapshot);
        cache.clear();
        cache.addAll(snapshot);
    }

    @Override
    public synchronized List<Student> findAll() {
        return copyStudents(cache);
    }

    @Override
    public synchronized Optional<Student> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return cache.stream()
                .filter(student -> id.equals(student.getId()))
                .findFirst()
                .map(this::copyStudent);
    }

    @Override
    public synchronized Student create(Student student) {
        Validation.requireNotNull(student, "Student");
        Validation.requireNonBlank(student.getId(), "Student ID");

        boolean exists = cache.stream().anyMatch(s -> s.getId().equals(student.getId()));
        if (exists) {
            throw new ValidationException("Un étudiant avec l'ID " + student.getId() + " existe déjà.");
        }

        Student stored = copyStudent(student);
        cache.add(stored);
        writeToFile(cache);
        return copyStudent(stored);
    }

    @Override
    public synchronized Student update(Student student) {
        Validation.requireNotNull(student, "Student");
        Validation.requireNonBlank(student.getId(), "Student ID");

        int index = indexOf(student.getId());
        if (index < 0) {
            throw new NotFoundException("Étudiant introuvable pour mise à jour : " + student.getId());
        }

        Student stored = copyStudent(student);
        cache.set(index, stored);
        writeToFile(cache);
        return copyStudent(stored);
    }

    @Override
    public synchronized void delete(String id) {
        Validation.requireNonBlank(id, "Student ID");
        int index = indexOf(id);
        if (index < 0) {
            throw new NotFoundException("Étudiant introuvable pour suppression : " + id);
        }
        cache.remove(index);
        writeToFile(cache);
    }

    private int indexOf(String id) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private List<Student> copyStudents(List<Student> source) {
        return source.stream()
                .map(this::copyStudent)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Student copyStudent(Student original) {
        List<CourseNote> notes = original.getNotes().stream()
                .map(n -> new CourseNote(n.getCourseName(), n.getNote()))
                .collect(Collectors.toCollection(ArrayList::new));
        return new Student(original.getId(), original.getName(), original.getEmail(), notes);
    }

    private void ensureFileExists() throws IOException {
        Path parent = path.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(path)) {
            Files.write(path, List.of(), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        }
    }

    private void writeToFile(List<Student> students) {
        try {
            ensureFileExists();
            List<String> lines = serializer.format(students);
            Files.write(path, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new PersistenceException("Erreur lors de l'écriture du fichier " + path, e);
        }
    }
}
