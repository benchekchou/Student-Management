package hamza.patient.net.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student {
    private String id;
    private String name;
    private String email;
    private List<CourseNote> notes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Student(String id) {
        this.id = id;
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
        return notes;
    }

    public void setNotes(List<CourseNote> notes) {
        this.notes = notes;
    }
    // getters/setters/constructeurs
    // equals/hashCode/toStrin

    public Student(String id, String name, String email, List<CourseNote> notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(name, student.name) && Objects.equals(email, student.email) && Objects.equals(notes, student.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, notes);
    }
}
