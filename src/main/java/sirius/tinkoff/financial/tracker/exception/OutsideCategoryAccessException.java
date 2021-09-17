package sirius.tinkoff.financial.tracker.exception;

public class OutsideCategoryAccessException extends OutsideObjectAccessException {
    private static final String MSG = "Unable to get access to another user's category";

    public OutsideCategoryAccessException() {
        super(MSG);
    }
}
