package hamza.patient.net.config;

import hamza.patient.net.console.*;
import hamza.patient.net.repository.StudentRepository;
import hamza.patient.net.repository.file.*;
import hamza.patient.net.service.CalculationService;
import hamza.patient.net.service.CourseNoteService;
import hamza.patient.net.service.StudentService;
import hamza.patient.net.service.strategy.SimpleAverageStrategy;
import hamza.patient.net.util.Logger;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;


public class AppConfig {
    /** Clé de propriété/VM arg : -Dstudents.file=/chemin/vers/students.txt */
    public static final String PROP_STUDENTS_FILE = "students.file";
    /** Variable d'environnement alternative */
    public static final String ENV_STUDENTS_FILE  = "STUDENTS_FILE";
    /** Clé possible dans src/main/resources/application.properties */
    public static final String CFG_STUDENTS_FILE  = "students.file";

    private final Properties props = new Properties();

    public AppConfig() {
        loadPropertiesIfPresent();
    }

    /** Permet de forcer un chemin custom dans les tests si besoin. */
    public ConsoleApplication buildApp(Path customDataPath) {
        Path dataPath = Objects.requireNonNullElseGet(customDataPath, this::resolveDataPath);
        return wire(dataPath);
    }

    /** Construction standard (utilise la résolution de chemin). */
    public ConsoleApplication buildApp() {
        return wire(resolveDataPath());
    }

    // -------------------- wiring interne --------------------

    private ConsoleApplication wire(Path dataPath) {
        Logger.info("Fichier de données : " + dataPath.toAbsolutePath());

        // Repository (TXT sérialisation, 100% JDK)
        TxtSerializer serializer = new TxtSerializer();
        StudentRepository repository = new FileStudentRepository(dataPath, serializer);

        // Services
        StudentService studentService       = new StudentService(repository);
        CourseNoteService courseNoteService = new CourseNoteService(repository);
        CalculationService calcService      = new CalculationService(repository, new SimpleAverageStrategy());

        // Views console
        StudentViews studentViews         = new StudentViews(studentService);
        CourseViews courseViews           = new CourseViews(studentService, courseNoteService);
        CalculationViews calculationViews = new CalculationViews(calcService);

        // Menu (point d'entrée UI console)
        Menu menu = new Menu(studentViews, courseViews, calculationViews);

        return new ConsoleApplication(menu);
    }

    // -------------------- résolution du chemin --------------------

    private Path resolveDataPath() {
        // 1) Priorité aux arguments JVM : -Dstudents.file=...
        String jvmProp = System.getProperty(PROP_STUDENTS_FILE);
        if (isNonEmpty(jvmProp)) {
            return ensureParentExists(Paths.get(jvmProp.trim()));
        }

        // 2) Puis variable d'environnement STUDENTS_FILE
        String env = System.getenv(ENV_STUDENTS_FILE);
        if (isNonEmpty(env)) {
            return ensureParentExists(Paths.get(env.trim()));
        }

        // 3) Puis application.properties (si présent dans le classpath)
        String fromCfg = props.getProperty(CFG_STUDENTS_FILE);
        if (isNonEmpty(fromCfg)) {
            return ensureParentExists(Paths.get(fromCfg.trim()));
        }

        // 4) Par défaut : fichier local "students.txt" dans le répertoire courant
        return ensureParentExists(Paths.get("students.txt"));
    }

    private void loadPropertiesIfPresent() {
        try (InputStream in = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in != null) {
                props.load(in);
                Logger.info("application.properties chargé depuis le classpath.");
            }
        } catch (Exception e) {
            // Non bloquant : on reste silencieux côté utilisateur
            Logger.warn("Impossible de charger application.properties : " + e.getMessage());
        }
    }

    private static boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /** Crée le répertoire parent si nécessaire, retourne le path normalisé. */
    private static Path ensureParentExists(Path p) {
        try {
            Path parent = p.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
        } catch (Exception e) {
            Logger.warn("Impossible de créer le répertoire parent pour " + p + " : " + e.getMessage());
        }
        return p;
    }
}
