package hamza.patient.net.console;

import hamza.patient.net.domain.CourseNote;
import hamza.patient.net.domain.Student;
import hamza.patient.net.exceptions.StudentManagementException;
import hamza.patient.net.service.CourseNoteService;
import hamza.patient.net.service.StudentService;

import java.util.List;
import java.util.Scanner;

public class CourseViews {
    private final StudentService studentService;
    private final CourseNoteService courseNoteService;

    public CourseViews(StudentService studentService, CourseNoteService courseNoteService) {
        this.studentService = studentService;
        this.courseNoteService = courseNoteService;
    }

    public void manageCourses(Scanner scanner) {
        System.out.print("ID de l'étudiant: ");
        String studentId = scanner.nextLine();
        Student student;
        try {
            student = studentService.getStudent(studentId);
        } catch (StudentManagementException ex) {
            System.out.println("[ERREUR] " + ex.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            System.out.printf("-- Notes de %s (%s) --%n", student.getName(), student.getId());
            printNotes(student);
            System.out.println("1. Ajouter / Mettre à jour une note");
            System.out.println("2. Supprimer une note");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addOrUpdate(scanner, student);
                case "2" -> remove(scanner, student);
                case "0" -> running = false;
                default -> System.out.println("Choix invalide.");
            }
            if (running) {
                student = studentService.getStudent(student.getId());
            }
        }
    }

    private void addOrUpdate(Scanner scanner, Student student) {
        try {
            System.out.print("Nom du cours: ");
            String course = scanner.nextLine();
            System.out.print("Note (0-20): ");
            String raw = scanner.nextLine();
            double note = Double.parseDouble(raw);
            courseNoteService.addOrUpdateCourseNote(student.getId(), course, note);
            System.out.println("Note enregistrée.");
        } catch (NumberFormatException ex) {
            System.out.println("[ERREUR] Format numérique invalide.");
        } catch (StudentManagementException ex) {
            System.out.println("[ERREUR] " + ex.getMessage());
        }
    }

    private void remove(Scanner scanner, Student student) {
        try {
            System.out.print("Nom du cours à supprimer: ");
            String course = scanner.nextLine();
            courseNoteService.removeCourseNote(student.getId(), course);
            System.out.println("Note supprimée.");
        } catch (StudentManagementException ex) {
            System.out.println("[ERREUR] " + ex.getMessage());
        }
    }

    private void printNotes(Student student) {
        List<CourseNote> notes = courseNoteService.listNotes(student.getId());
        if (notes.isEmpty()) {
            System.out.println("   Aucune note enregistrée.");
            return;
        }

        for (CourseNote note : notes) {
            System.out.printf("   - %s : %.2f%n", note.getCourseName(), note.getNote());
        }
    }
}
