package hamza.patient.net.console;

import hamza.patient.net.domain.Student;
import hamza.patient.net.exceptions.StudentManagementException;
import hamza.patient.net.service.CalculationService;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Scanner;

public class CalculationViews {
    private final CalculationService calculationService;

    public CalculationViews(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public void open(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("-- Statistiques --");
            System.out.println("1. Moyenne d'un étudiant");
            System.out.println("2. Meilleur étudiant");
            System.out.println("3. Étudiants en échec (< 10)");
            System.out.println("4. Moyenne générale");
            System.out.println("0. Retour");
            System.out.print("Choix: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showStudentAverage(scanner);
                case "2" -> showBestStudent();
                case "3" -> showFailingStudents();
                case "4" -> showGlobalAverage();
                case "0" -> running = false;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void showStudentAverage(Scanner scanner) {
        System.out.print("ID de l'étudiant: ");
        String id = scanner.nextLine();
        try {
            Double average = calculationService.computeAverage(id);
            if (average == null) {
                System.out.println("Pas de cours enregistrés pour cet étudiant.");
            } else {
                System.out.printf("Moyenne : %.2f%n", average);
            }
        } catch (StudentManagementException ex) {
            System.out.println("[ERREUR] " + ex.getMessage());
        }
    }

    private void showBestStudent() {
        Optional<Student> best = calculationService.getBestStudent();
        if (best.isEmpty()) {
            System.out.println("Aucun étudiant avec des notes.");
            return;
        }
        Student student = best.get();
        Double average = calculationService.computeAverage(student.getId());
        System.out.printf("Meilleur étudiant : %s (ID: %s) - Moyenne %.2f%n",
                student.getName(), student.getId(), average);
    }

    private void showFailingStudents() {
        List<Student> failing = calculationService.getFailingStudents();
        if (failing.isEmpty()) {
            System.out.println("Aucun étudiant en échec.");
            return;
        }
        System.out.println("-- Étudiants en échec --");
        failing.forEach(student -> {
            Double avg = calculationService.computeAverage(student.getId());
            System.out.printf("%s (ID: %s) - Moyenne %.2f%n",
                    student.getName(), student.getId(), avg);
        });
    }

    private void showGlobalAverage() {
        OptionalDouble average = calculationService.computeGlobalAverage();
        if (average.isEmpty()) {
            System.out.println("Aucune donnée disponible.");
        } else {
            System.out.printf("Moyenne générale: %.2f%n", average.getAsDouble());
        }
    }
}
