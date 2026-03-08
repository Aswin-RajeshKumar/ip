/**
 * Represents errors specific to the Nova application's domain logic.
 * This class allows the application to distinguish between general Java exceptions
 * and anticipated task-management errors (e.g., invalid command formats).
 */
public class NovaException extends Exception {

    /**
     * Constructs a new NovaException with a specific error message.
     *
     * @param message A descriptive error message explaining the cause of the exception.
     */
    public NovaException(String message) {
        super(message);
    }
}