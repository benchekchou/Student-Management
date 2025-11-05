package hamza.patient.net.domain;

import java.util.ArrayList;
import java.util.List;

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
    // equals/hashCode/toString

}
