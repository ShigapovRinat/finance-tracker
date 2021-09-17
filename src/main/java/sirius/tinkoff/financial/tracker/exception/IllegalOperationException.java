package sirius.tinkoff.financial.tracker.exception;

public class IllegalOperationException extends ClientMessageException {
    public IllegalOperationException(String message) {
        super(message);
    }
}
