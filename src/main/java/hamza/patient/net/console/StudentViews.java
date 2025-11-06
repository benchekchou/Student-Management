package hamza.patient.net.console;

import hamza.patient.net.domain.Student;
import hamza.patient.net.exceptions.StudentManagementException;
import hamza.patient.net.service.StudentService;

import java.util.List;
import java.util.Scanner;

public class StudentViews {
    private final StudentService studentService;

    public StudentViews(StudentService studentService) {
        this.studentService = studentService;
    }

    public void listStudents() {
        List<Student> students = studentService.listStudents();
        if (students.isEmpty()) {
            System.out.println("Aucun étudiant enregistré.");
            return;
        }
        System.out.println("-- Étudiants --");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("%d. %s (%s) - %d cours%n",
                    i + 1,
                    safe(s.getName()),
                    safe(s.getEmail()),
                    s.getNotes().size());
            System.out.printf("   ID: %s%n", s.getId());
        }
    }

    public void createStudent(Scanner scanner) {
        try {
            System.out.print("Nom: ");
            String name = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            Student created = studentService.createStudent(name, email);
            System.out.println("Étudiant créé avec succès. ID = " + created.getId());
        } catch (StudentManagementException ex) {
            System.out.println("[ERREUR] " + ex.getMessage());
        }
    }

    public void updateStudent(Scanner scanner) {
        try {
            System.out.print("ID de l'étudiant à mettre à jour: ");
            String id = scanner.nextLine();
            System.out.print("Nouveau nom (laisser vide pour conserver) : ");
            String name = scanner.nextLine();
            System.out.print("Nouvel email (laisser vide pour conserver) : ");
            String email = scanner.nextLine();

            Student updated = studentService.updateStudent(id, name, email);
            System.out.println("Étudiant mis à jour: " + safe(updated.getName()) + " (" + safe(updated.getEmail()) + ")");
        } catch (StudentManagementException ex) {
            System.out.println("[ERREUR] " + ex.getMessage());
        }
    }

    public void deleteStudent(Scanner scanner) {
        try {
            System.out.print("ID de l'étudiant à supprimer: ");
            String id = scanner.nextLine();
            studentService.deleteStudent(id);
            System.out.println("Étudiant supprimé.");
        } catch (StudentManagementException ex) {
            System.out.println("[ERREUR] " + ex.getMessage());
        }
    }

    public void searchStudents(Scanner scanner) {
        System.out.print("Rechercher (nom ou email): ");
        String query = scanner.nextLine();
        List<Student> results = studentService.searchStudents(query);
        if (results.isEmpty()) {
            System.out.println("Aucun résultat trouvé.");
            return;
        }
        System.out.println("-- Résultats de recherche --");
        results.forEach(student -> System.out.printf("%s (%s) - ID: %s%n",
                safe(student.getName()),
                safe(student.getEmail()),
                student.getId()));
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "<non défini>" : value;
    }
}
