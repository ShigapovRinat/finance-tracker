package sirius.tinkoff.financial.tracker.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sirius.tinkoff.financial.tracker.exception.ClientMessageException;
import sirius.tinkoff.financial.tracker.exception.InsufficientFundsException;
import sirius.tinkoff.financial.tracker.exception.OutsideObjectAccessException;

@Log4j2
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ClientMessageException.class)
    public ResponseEntity<ResponseMessage> handleException(ClientMessageException e) {
        ResponseMessage responseMessage = new ResponseMessage(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OutsideObjectAccessException.class)
    public ResponseEntity<ResponseMessage> handleException(OutsideObjectAccessException e) {
        ResponseMessage responseMessage = new ResponseMessage(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ResponseMessage> handleException(InsufficientFundsException e) {
        ResponseMessage responseMessage = new ResponseMessage(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }
}
