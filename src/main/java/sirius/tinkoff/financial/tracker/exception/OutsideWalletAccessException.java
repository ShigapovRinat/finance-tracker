package sirius.tinkoff.financial.tracker.exception;

public class OutsideWalletAccessException extends OutsideObjectAccessException {

    private final static String MSG = "Unable to get access to another user's wallet";

    public OutsideWalletAccessException() {
        super(MSG);
    }
}
