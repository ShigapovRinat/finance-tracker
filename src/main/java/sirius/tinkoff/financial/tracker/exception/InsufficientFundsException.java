package sirius.tinkoff.financial.tracker.exception;

public class InsufficientFundsException extends ClientMessageException {

    private final static String MSG = "Insufficient funds";

    public InsufficientFundsException() {
        super(MSG);
    }
}