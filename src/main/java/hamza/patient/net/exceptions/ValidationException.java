package hamza.patient.net.exceptions;

/**
 * Raised when user input or domain data violates validation rules.
 */
public class ValidationException extends StudentManagementException {
    public ValidationException(String message) {
        super(message);
    }
}
