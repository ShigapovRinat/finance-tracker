package sirius.tinkoff.financial.tracker.exception;

public class UnableToFindCategoryException extends ClientMessageException {

    private final static String MSG = "Unable to find category with given ID";

    public UnableToFindCategoryException() {
        super(MSG);
    }
}
