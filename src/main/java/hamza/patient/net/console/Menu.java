package hamza.patient.net.console;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final StudentViews studentViews;
    private final CourseViews courseViews;
    private final CalculationViews calculationViews;

    public Menu(StudentViews studentViews, CourseViews courseViews, CalculationViews calculationViews) {
        this.studentViews = studentViews;
        this.courseViews = courseViews;
        this.calculationViews = calculationViews;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Choix: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> studentViews.listStudents();
                case "2" -> studentViews.createStudent(scanner);
                case "3" -> studentViews.updateStudent(scanner);
                case "4" -> studentViews.deleteStudent(scanner);
                case "5" -> studentViews.searchStudents(scanner);
                case "6" -> courseViews.manageCourses(scanner);
                case "7" -> calculationViews.open(scanner);
                case "0" -> {
                    System.out.println("Au revoir!");
                    running = false;
                }
                default -> System.out.println("Choix invalide.");
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("=== Gestion des étudiants ===");
        System.out.println("1. Lister les étudiants");
        System.out.println("2. Ajouter un étudiant");
        System.out.println("3. Mettre à jour un étudiant");
        System.out.println("4. Supprimer un étudiant");
        System.out.println("5. Rechercher un étudiant");
        System.out.println("6. Gérer les notes d'un étudiant");
        System.out.println("7. Statistiques");
        System.out.println("0. Quitter");
    }
}
