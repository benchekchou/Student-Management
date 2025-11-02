package hamza.patient.net.service;

import hamza.patient.net.domain.Student;
import hamza.patient.net.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

public class CalculationService {

    private final StudentRepository repo;

    public CalculationService(StudentRepository repo) {
        this.repo = repo;
    }

    /**
     * Calcule la moyenne d’un étudiant.
     * @return Double arrondi à 2 décimales, ou null si l’étudiant n’a aucun cours.
     */
    public Double computeAverage(String studentId) {

        return 0.0;
    }

    /** Retourne l’étudiant avec la meilleure moyenne (tiebreak appliqué). */
    public Optional<Student> getBestStudent() {

        return Optional.empty();
    }

    /**
     * Retourne la liste des étudiants en échec (moyenne STRICTEMENT < 10),
     * triée par moyenne croissante puis par nom.
     */
    public List<Student> getFailingStudents() {

        return List.of();
    }

    // ----- Helpers -----

    /** Moyenne arrondie à 2 décimales, ou null si aucun cours. */
    private Double averageOf(Student s) {

        return 0.0;
    }

    /** Moyenne ou 0.0 si N/A (usage pour tri uniquement). */
    private Double averageOfOrZero(Student s) {

        return 0.0;
    }
}
