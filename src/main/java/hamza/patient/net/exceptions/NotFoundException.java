package hamza.patient.net.exceptions;

/**
 * Indicates that an entity could not be found in repositories/services.
 */
public class NotFoundException extends StudentManagementException {
    public NotFoundException(String message) {
        super(message);
    }
}
