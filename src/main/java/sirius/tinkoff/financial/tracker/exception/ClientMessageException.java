package sirius.tinkoff.financial.tracker.exception;

public class ClientMessageException extends RuntimeException {

    private final String msg;

    public ClientMessageException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ClientMessageException(String msg, Throwable throwable) {
        super(msg, throwable);
        this.msg = msg;
    }
}
