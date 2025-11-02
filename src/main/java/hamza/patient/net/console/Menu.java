package hamza.patient.net.console;

import java.util.Scanner;

public class Menu {
    private final Scanner sc = new Scanner(System.in);

    public Menu(StudentViews studentViews, CourseViews courseViews, CalculationViews calculationViews) {
    }
    // références vers services + views

    public void start() {
        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": /* StudentViews.add() */ break;
                // ...
                case "0": System.out.println("Au revoir!"); return;
                default: System.out.println("[ERREUR] Choix invalide.");
            }
        }
    }
    private void printMenu() { /* ... */ }
}
