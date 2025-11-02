package hamza.patient.net.service;

import hamza.patient.net.domain.Student;
import hamza.patient.net.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }
    public Student createStudent(String name, String email){

        return null;
    }

    /** Retourne la liste des étudiants (triée par nom puis ID pour stabilité). */
    public List<Student> listStudents() {

        return List.of();
    }



    /**
     * Met à jour le nom/email d’un étudiant. Les champs vides ou null ne sont pas modifiés.
     */
    public Student updateStudent(String id, String newNameOrNull, String newEmailOrNull) {

        return null;
    }

    /** Supprime un étudiant par ID (exception si non trouvé). */
    public void deleteStudent(String id) {

    }

    /** (Optionnel) Recherche simple sur nom/email (contient, case-insensitive). */
    public List<Student> searchStudents(String query) {

        return List.of();
    }
}
