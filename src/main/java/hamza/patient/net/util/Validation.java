package hamza.patient.net.util;

import java.util.regex.Pattern;

/**
 * Tâche 2 : Classe utilitaire pour la validation des données.
 * Lève 'IllegalArgumentException' si une règle n'est pas respectée.
 */
public class Validation {

    /**
     * Regex simple pour un email (requis par Tâche 2).
     * "static final" signifie que la regex est compilée une seule fois.
     * CASE_INSENSITIVE permet de tolérer majuscules et minuscules.
     */
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Constructeur privé (classe utilitaire).
     */
    private Validation() {
    }

    /**
     * Vérifie qu'un objet n'est pas nul.
     * (Requis par Tâche 2)
     *
     * @param v L'objet à tester.
     * @param field Le nom du champ (pour le message d'erreur).
     */
    public static void requireNotNull(Object v, String field) {
        if (v == null) {
            // Lève une exception si la validation échoue.
            throw new IllegalArgumentException(field + " ne peut pas être nul.");
        }
    }

    /**
     * Vérifie qu'une chaîne n'est ni nulle, ni vide (après avoir enlevé les espaces).
     * (Requis par Tâche 2)
     *
     * @param v La chaîne à tester.
     * @param field Le nom du champ (pour le message d'erreur).
     */
    public static void requireNonBlank(String v, String field) {
        requireNotNull(v, field); // On réutilise la validation "non nul"
        if (v.trim().isEmpty()) { // trim() enlève les espaces avant/après
            throw new IllegalArgumentException(field + " ne peut pas être vide.");
        }
    }

    /**
     * Vérifie le format de l'email.
     * (Requis par Tâche 2)
     *
     * @param email L'email à tester.
     */
    public static void requireEmail(String email) {
        requireNonBlank(email, "Email");

        // On utilise la regex (Critère d'acceptation "alice@" échouera)
        if (!EMAIL_REGEX.matcher(email.trim()).matches()) {
            // Message explicite (Critère d'acceptation Tâche 2)
            throw new IllegalArgumentException("Le format de l'email est invalide : " + email);
        }
    }

    /**
     * Vérifie qu'un nombre est dans un intervalle (ex: notes 0-20).
     * (Requis par Tâche 2)
     *
     * @param value La valeur (ex: 21).
     * @param min La borne min (ex: 0).
     * @param max La borne max (ex: 20).
     * @param field Le nom du champ (pour le message d'erreur).
     */
    public static void requireRange(double value, double min, double max, String field) {
        if (value < min || value > max) {
            // Message explicite (Critère d'acceptation Tâche 2: "exception")
            throw new IllegalArgumentException(
                    String.format("%s doit être compris entre %.2f et %.2f (valeur reçue: %.2f)", field, min, max, value)
            );
        }
    }
}