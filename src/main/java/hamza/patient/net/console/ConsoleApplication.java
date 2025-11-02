package hamza.patient.net.console;


public class ConsoleApplication {
    private final Menu menu;

    public ConsoleApplication(Menu menu) {
        this.menu = menu;
    }

    /** Lance la boucle du menu en console. */
    public void start() {
        menu.start();
    }

    /** Expose le menu si besoin de tests d'intégration. */
    public Menu getMenu() {
        return menu;
    }
}
