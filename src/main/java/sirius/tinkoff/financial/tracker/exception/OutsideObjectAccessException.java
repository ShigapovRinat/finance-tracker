package sirius.tinkoff.financial.tracker.exception;

public class OutsideObjectAccessException extends RuntimeException {
    private final String msg;

    public OutsideObjectAccessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public OutsideObjectAccessException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
    }
}
