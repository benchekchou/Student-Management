package hamza.patient.net.service;

import hamza.patient.net.repository.StudentRepository;

public class CourseNoteService {
    private final StudentRepository repo;

    public CourseNoteService(StudentRepository repo) {
        this.repo = repo;
    }

    /** Ajoute ou met à jour une note de cours pour un étudiant. */
    public void addOrUpdateCourseNote(String studentId, String courseName, double note) {

    }

    /** Supprime un cours d’un étudiant. Exception si le cours n’existe pas. */
    public void removeCourseNote(String studentId, String courseName) {

    }
}
