package hamza.patient.net.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public enum Level { DEBUG, INFO, WARN, ERROR }

    // --------- Configuration ---------

    private static volatile Level MIN_LEVEL = resolveMinLevel();
    private static volatile boolean ANSI_ENABLED = resolveAnsiEnabled();

    private static final DateTimeFormatter TS_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Couleurs ANSI (optionnelles)
    private static final String RESET = "\u001B[0m";
    private static final String GRAY  = "\u001B[90m";
    private static final String CYAN  = "\u001B[36m";
    private static final String YELL  = "\u001B[33m";
    private static final String RED   = "\u001B[31m";

    private Logger() { /* no-op */ }

    // --------- API publique ---------

    public static void setMinLevel(Level level) {
        if (level != null) MIN_LEVEL = level;
    }

    public static Level getMinLevel() {
        return MIN_LEVEL;
    }

    public static void setAnsiEnabled(boolean enabled) {
        ANSI_ENABLED = enabled;
    }

    public static boolean isAnsiEnabled() {
        return ANSI_ENABLED;
    }

    // --- INFO ---
    public static void info(String message) {
        log(Level.INFO, message, null);
    }

    public static void info(String fmt, Object... args) {
        log(Level.INFO, format(fmt, args), null);
    }

    // --- WARN ---
    public static void warn(String message) {
        log(Level.WARN, message, null);
    }

    public static void warn(String fmt, Object... args) {
        log(Level.WARN, format(fmt, args), null);
    }

    // --- ERROR ---
    public static void error(String message) {
        log(Level.ERROR, message, null);
    }

    public static void error(String fmt, Object... args) {
        log(Level.ERROR, format(fmt, args), null);
    }

    public static void error(String message, Throwable t) {
        log(Level.ERROR, message, t);
    }

    // --- DEBUG ---
    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }

    public static void debug(String fmt, Object... args) {
        log(Level.DEBUG, format(fmt, args), null);
    }

    // --------- Impl interne ---------

    private static boolean isEnabled(Level lvl) {
        return lvl.ordinal() >= MIN_LEVEL.ordinal();
    }

    private static String nowTs() {
        return LocalDateTime.now().format(TS_FMT);
    }

    private static String format(String fmt, Object... args) {
        try {
            return String.format(fmt, args);
        } catch (Exception e) {
            // En cas de mauvais format, on retombe sur la concaténation simple
            StringBuilder sb = new StringBuilder(String.valueOf(fmt));
            if (args != null && args.length > 0) {
                sb.append(" | args=");
                for (int i = 0; i < args.length; i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(String.valueOf(args[i]));
                }
            }
            return sb.toString();
        }
    }

    private static String colorize(Level lvl, String text) {
        if (!ANSI_ENABLED) return text;
        return switch (lvl) {
            case DEBUG -> GRAY + text + RESET;
            case INFO  -> CYAN + text + RESET;
            case WARN  -> YELL + text + RESET;
            case ERROR -> RED  + text + RESET;
        };
    }

    private static String stackTraceToString(Throwable t) {
        if (t == null) return "";
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private static synchronized void log(Level lvl, String message, Throwable t) {
        if (!isEnabled(lvl)) return;

        String ts = nowTs();
        String header = "[" + ts + "] [" + lvl.name() + "] ";
        String body = message == null ? "" : message;

        String line = header + body;
        String colored = colorize(lvl, line);

        switch (lvl) {
            case ERROR -> {
                System.err.println(colored);
                if (t != null) {
                    String st = stackTraceToString(t);
                    System.err.println(ANSI_ENABLED ? colorize(Level.ERROR, st) : st);
                }
            }
            default -> {
                System.out.println(colored);
                if (t != null) {
                    String st = stackTraceToString(t);
                    System.out.println(ANSI_ENABLED ? colorize(Level.ERROR, st) : st);
                }
            }
        }
    }

    // --------- Résolution config ---------

    private static Level resolveMinLevel() {
        String fromSys = System.getProperty("log.level");
        String fromEnv = System.getenv("LOG_LEVEL");
        String raw = (fromSys != null) ? fromSys : fromEnv;
        if (raw == null) return Level.INFO;
        try {
            return Level.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return Level.INFO;
        }
    }

    private static boolean resolveAnsiEnabled() {
        String fromSys = System.getProperty("log.ansi");
        String fromEnv = System.getenv("LOG_ANSI");
        String raw = (fromSys != null) ? fromSys : fromEnv;
        if (raw == null) {
            // Heuristique: activer par défaut sauf si Windows classique
            String os = System.getProperty("os.name", "").toLowerCase();
            return !os.contains("win");
        }
        return raw.trim().equalsIgnoreCase("true") ||
                raw.trim().equalsIgnoreCase("1") ||
                raw.trim().equalsIgnoreCase("yes") ||
                raw.trim().equalsIgnoreCase("y");
    }
}
