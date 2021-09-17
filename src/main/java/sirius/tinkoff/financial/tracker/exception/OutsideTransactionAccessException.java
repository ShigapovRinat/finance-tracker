package sirius.tinkoff.financial.tracker.exception;

public class OutsideTransactionAccessException extends OutsideObjectAccessException {
    private final static String MSG = "Unable to get access to another user's transaction";

    public OutsideTransactionAccessException() {
        super(MSG);
    }
}
