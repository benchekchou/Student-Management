package hamza.patient.net.domain;

import hamza.patient.net.util.Validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Aggregate root representing a student with its course notes.
 */
public class Student {
    private String id;
    private String name;
    private String email;
    private final List<CourseNote> notes = new ArrayList<>();

    public Student() {
    }

    public Student(String id, String name, String email) {
        this(id, name, email, List.of());
    }

    public Student(String id, String name, String email, List<CourseNote> notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        setNotes(notes);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CourseNote> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public void setNotes(List<CourseNote> notes) {
        this.notes.clear();
        if (notes != null) {
            this.notes.addAll(notes);
        }
    }

    public CourseNote addOrUpdateNote(String courseName, double note) {
        Validation.requireNonBlank(courseName, "Nom du cours");
        Validation.requireRange(note, 0, 20, "Note");

        Optional<CourseNote> existing = findNoteByCourse(courseName);
        if (existing.isPresent()) {
            existing.get().setNote(note);
            return existing.get();
        }

        CourseNote created = new CourseNote(courseName, note);
        notes.add(created);
        return created;
    }

    public boolean removeNoteByCourse(String courseName) {
        if (courseName == null) {
            return false;
        }
        return notes.removeIf(n -> n.getCourseName().equalsIgnoreCase(courseName.trim()));
    }

    public Optional<CourseNote> findNoteByCourse(String courseName) {
        if (courseName == null) {
            return Optional.empty();
        }
        return notes.stream()
                .filter(n -> n.getCourseName().equalsIgnoreCase(courseName.trim()))
                .findFirst();
    }

    public OptionalDouble average() {
        if (notes.isEmpty()) {
            return OptionalDouble.empty();
        }
        double sum = notes.stream().mapToDouble(CourseNote::getNote).sum();
        return OptionalDouble.of(sum / notes.size());
    }

    public boolean hasCourses() {
        return !notes.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{id='%s', name='%s', email='%s', courses=%d}"
                .formatted(id, name, email, notes.size());
    }
}
