package hamza.patient.net.domain;

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
}
