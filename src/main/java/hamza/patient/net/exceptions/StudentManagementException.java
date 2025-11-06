package hamza.patient.net.exceptions;

/**
 * Base unchecked exception for the Student Management application.
 */
public class StudentManagementException extends RuntimeException {
    public StudentManagementException(String message) {
        super(message);
    }

    public StudentManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}
