package hamza.patient.net.util;

import hamza.patient.net.exceptions.ValidationException;

import java.util.regex.Pattern;

/**
 * Validation helpers throwing {@link ValidationException} on failure.
 */
public final class Validation {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    private Validation() {
    }

    public static void requireNotNull(Object value, String field) {
        if (value == null) {
            throw new ValidationException(field + " ne peut pas être nul.");
        }
    }

    public static void requireNonBlank(String value, String field) {
        requireNotNull(value, field);
        if (value.trim().isEmpty()) {
            throw new ValidationException(field + " ne peut pas être vide.");
        }
    }

    public static void requireEmail(String email) {
        requireNonBlank(email, "Email");
        if (!EMAIL_REGEX.matcher(email.trim()).matches()) {
            throw new ValidationException("Le format de l'email est invalide : " + email);
        }
    }

    public static void requireRange(double value, double min, double max, String field) {
        if (value < min || value > max) {
            throw new ValidationException(
                    String.format("%s doit être compris entre %.2f et %.2f (valeur reçue: %.2f)", field, min, max, value)
            );
        }
    }
}
