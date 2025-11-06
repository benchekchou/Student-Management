package hamza.patient.net.domain;

import java.util.Objects;

/**
 * Value object representing a course grade.
 */
public class CourseNote {
    private String courseName;
    private double note; // 0..20

    public CourseNote() {
    }

    public CourseNote(String courseName, double note) {
        this.courseName = courseName;
        this.note = note;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseNote that = (CourseNote) o;
        return Double.compare(that.note, note) == 0 &&
                Objects.equals(courseName, that.courseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, note);
    }

    @Override
    public String toString() {
        return courseName + "=" + note;
    }
}
