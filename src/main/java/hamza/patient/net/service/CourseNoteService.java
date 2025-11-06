package hamza.patient.net.service;

import hamza.patient.net.domain.CourseNote;
import hamza.patient.net.domain.Student;
import hamza.patient.net.exceptions.NotFoundException;
import hamza.patient.net.repository.StudentRepository;
import hamza.patient.net.util.Validation;

import java.util.List;

/**
 * Business logic for managing course grades on students.
 */
public class CourseNoteService {
    private final StudentRepository repository;

    public CourseNoteService(StudentRepository repository) {
        Validation.requireNotNull(repository, "StudentRepository");
        this.repository = repository;
    }

    public CourseNote addOrUpdateCourseNote(String studentId, String courseName, double note) {
        Student student = loadStudent(studentId);
        CourseNote updated = student.addOrUpdateNote(courseName, note);
        repository.update(student);
        return updated;
    }

    public void removeCourseNote(String studentId, String courseName) {
        Student student = loadStudent(studentId);
        boolean removed = student.removeNoteByCourse(courseName);
        if (!removed) {
            throw new NotFoundException("Cours introuvable '" + courseName + "' pour l'étudiant " + studentId);
        }
        repository.update(student);
    }

    public List<CourseNote> listNotes(String studentId) {
        return loadStudent(studentId).getNotes();
    }

    private Student loadStudent(String studentId) {
        Validation.requireNonBlank(studentId, "Student ID");
        return repository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Étudiant introuvable : " + studentId));
    }
}
