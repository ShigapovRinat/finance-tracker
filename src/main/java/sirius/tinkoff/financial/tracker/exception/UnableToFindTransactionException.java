package sirius.tinkoff.financial.tracker.exception;

public class UnableToFindTransactionException extends ClientMessageException {

    private final static String MSG = "Unable to find transaction with given ID";

    public UnableToFindTransactionException() {
        super(MSG);
    }
}
