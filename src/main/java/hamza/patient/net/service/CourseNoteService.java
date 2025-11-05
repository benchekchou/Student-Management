package hamza.patient.net.service;

import hamza.patient.net.domain.CourseNote;
import hamza.patient.net.domain.Student;
import hamza.patient.net.repository.StudentRepository;
import hamza.patient.net.util.Validation;

import java.util.Optional;

/**
 * Service gérant la logique métier pour les notes de cours des étudiants.
 */
public class CourseNoteService {
    private final StudentRepository repo;

    public CourseNoteService(StudentRepository repo) {
        // Valider que le repository est bien fourni
        Validation.requireNotNull(repo, "StudentRepository");
        this.repo = repo;
    }

    // --- Méthode privée (Helper) ---

    /**
     * Méthode privée pour récupérer un étudiant par son ID.
     * Lève une exception claire si l'étudiant n'est pas trouvé.
     * @param studentId L'ID de l'étudiant.
     * @return L'entité Student.
     */
    private Student findStudentById(String studentId) {
        Validation.requireNonBlank(studentId, "Student ID");
        // Cherche l'étudiant ou lève une exception s'il n'existe pas
        return repo.findById(studentId).orElseThrow(
                () -> new IllegalArgumentException("Étudiant non trouvé avec l'ID : " + studentId)
        );
    }

    /**
     * Ajoute ou met à jour une note de cours pour un étudiant.
     * (Implémentation de la tâche)
     */
    @Override
    public void addOrUpdateCourseNote(String studentId, String courseName, double note) {
        // 1. Validation des entrées (requis par la tâche)
        Validation.requireNonBlank(courseName, "Nom du cours");
        Validation.requireRange(note, 0, 20, "Note"); // Validation de la plage 0-20

        // 2. Récupérer l'étudiant (en utilisant le helper)
        Student student = findStudentById(studentId);

        // 3. Chercher si le cours existe déjà pour cet étudiant
        Optional<CourseNote> existingNote = student.getNotes().stream()
                .filter(n -> n.getCourseName().equalsIgnoreCase(courseName))
                .findFirst();

        if (existingNote.isPresent()) {
            // Le cours existe -> Mettre à jour la note (Update)
            existingNote.get().setNote(note);
        } else {
            // Le cours n'existe pas -> Ajouter une nouvelle note (Create)
            student.getNotes().add(new CourseNote(courseName, note));
        }

        // 4. Sauvegarder l'entité Student modifiée (garantit la cohérence)
        repo.update(student);
    }

    /**
     * Supprime un cours d’un étudiant. Exception si le cours n’existe pas.
     * (Implémentation de la tâche)
     */
    @Override
    public void removeCourseNote(String studentId, String courseName) {
        // 1. Validation
        Validation.requireNonBlank(courseName, "Nom du cours");

        // 2. Récupérer l'étudiant
        Student student = findStudentById(studentId);

        // 3. Supprimer la note de la liste en utilisant removeIf
        boolean removed = student.getNotes().removeIf(
                note -> note.getCourseName().equalsIgnoreCase(courseName)
        );

        // 4. Gérer l'exception (requis par la tâche)
        if (!removed) {
            // Si rien n'a été supprimé, le cours n'existait pas
            throw new IllegalArgumentException("Cours non trouvé '" + courseName + "' pour cet étudiant.");
        }

        // 5. Sauvegarder les changements sur l'entité Student
        repo.update(student);
    }
}