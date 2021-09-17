package sirius.tinkoff.financial.tracker.exception;

public class UnableToFindUserException extends ClientMessageException {

    private final static String MSG = "Unable to find client with given ID";

    public UnableToFindUserException() {
        super(MSG);
    }
}
