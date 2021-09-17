package sirius.tinkoff.financial.tracker.exception;

public class UnableToFindWalletException extends ClientMessageException {
    private final static String MSG = "Unable to find wallet with given ID";

    public UnableToFindWalletException() {
        super(MSG);
    }
}
