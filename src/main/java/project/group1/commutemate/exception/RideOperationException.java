package project.group1.commutemate.exception;

/** A business-rule failure safe to display as an error banner. */
public class RideOperationException extends RuntimeException {

    public RideOperationException(String message) {
        super(message);
    }
}
