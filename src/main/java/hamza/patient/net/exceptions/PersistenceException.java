package hamza.patient.net.exceptions;

/**
 * Used for wrapping low-level persistence issues.
 */
public class PersistenceException extends StudentManagementException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
