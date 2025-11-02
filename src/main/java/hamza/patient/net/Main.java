package hamza.patient.net;

import hamza.patient.net.config.AppConfig;


public class Main {
    public static void main(String[] args) {
        var app = new AppConfig().buildApp();
        app.start(); // lance Menu
    }
}