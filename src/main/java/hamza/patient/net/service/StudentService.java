package hamza.patient.net.service;

import hamza.patient.net.domain.Student;
import hamza.patient.net.repository.StudentRepository;
// On importe les utilitaires de la Tâche 2
import hamza.patient.net.util.IdGenerator;
import hamza.patient.net.util.Validation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service étudiant, suivant la structure demandée.
 * Utilise les Tâches 1 (logique) et 2 (validation).
 */
public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        // On valide que le repository est bien fourni (logique Tâche 2)
        Validation.requireNotNull(repo, "StudentRepository");
        this.repo = repo;
    }

    /**
     * Crée un nouvel étudiant.
     * (Logique basée sur Tâche 1 et 2)
     */
    public Student createStudent(String name, String email){
        // 1. Valider les entrées (logique Tâche 1 "nom non vide" et "email valide")
        // (On utilise la classe Validation de la Tâche 2)
        Validation.requireNonBlank(name, "Name");
        Validation.requireEmail(email); // Valide aussi nonBlank

        // 2. Créer l'objet Student
        Student newStudent = new Student();
        newStudent.setName(name.trim()); // On sauvegarde le nom sans espaces
        newStudent.setEmail(email.trim().toLowerCase()); // On sauvegarde l'email en minuscules

        // 3. Ajouter un ID (logique Tâche 1 "Ajoute id si manquant")
        newStudent.setId(IdGenerator.newId()); // Utilisation Tâche 2

        // 4. (Logique Tâche 1 "createdAt/updatedAt" non applicable car champs absents)

        // 5. Appeler le repository pour sauvegarder
        return repo.create(newStudent);
    }

    /** * Retourne la liste des étudiants (triée par nom puis ID pour stabilité).
     * (Logique basée sur Tâche 1 "tri lastName, firstName" adaptée à "name")
     */
    public List<Student> listStudents() {
        List<Student> students = repo.findAll();

        // Tri par 'name' (adaptation de Tâche 1) puis par 'id' (stabilité)
        students.sort(
                Comparator.comparing(Student::getName)
                        .thenComparing(Student::getId)
        );

        return students;
    }



    /**
     * Met à jour le nom/email d’un étudiant. Les champs vides ou null ne sont pas modifiés.
     * (Logique basée sur le commentaire de la structure)
     */
    public Student updateStudent(String id, String newNameOrNull, String newEmailOrNull) {
        // 1. Trouver l'étudiant existant (logique Tâche 1 "getById")
        Student existing = repo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Étudiant non trouvé avec l'ID : " + id)
        );

        boolean updated = false;

        // 2. Mettre à jour le nom (si fourni)
        if (newNameOrNull != null && !newNameOrNull.trim().isEmpty()) {
            Validation.requireNonBlank(newNameOrNull, "New Name"); // Validation Tâche 2
            existing.setName(newNameOrNull.trim());
            updated = true;
        }

        // 3. Mettre à jour l'email (si fourni)
        if (newEmailOrNull != null && !newEmailOrNull.trim().isEmpty()) {
            Validation.requireEmail(newEmailOrNull); // Validation Tâche 2
            existing.setEmail(newEmailOrNull.trim().toLowerCase());
            updated = true;
        }

        // 4. Sauvegarder uniquement si des changements ont eu lieu
        if (updated) {
            // (Logique Tâche 1 "met updatedAt" non applicable car champ absent)
            return repo.update(existing);
        }

        return existing; // Retourne l'original si pas de mise à jour
    }

    /** * Supprime un étudiant par ID (exception si non trouvé).
     * (Logique basée sur le commentaire de la structure)
     */
    public void deleteStudent(String id) {
        Validation.requireNonBlank(id, "Student ID");

        // 1. Vérifier que l'étudiant existe
        Student existing = repo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Étudiant non trouvé avec l'ID : " + id)
        );

        // 2. Supprimer
        repo.delete(existing.getId());
    }

    /** * (Optionnel) Recherche simple sur nom/email (contient, case-insensitive).
     */
    public List<Student> searchStudents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(); // Retourne une liste vide si la recherche est vide
        }

        String lowerQuery = query.trim().toLowerCase();

        return repo.findAll().stream() // On prend tous les étudiants
                .filter(student ->
                        // On vérifie si le nom OU l'email contient la recherche
                        (student.getName() != null && student.getName().toLowerCase().contains(lowerQuery)) ||
                                (student.getEmail() != null && student.getEmail().toLowerCase().contains(lowerQuery))
                )
                .collect(Collectors.toList()); // On retourne une nouvelle liste
    }
}