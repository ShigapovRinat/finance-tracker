package sirius.tinkoff.financial.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sirius.tinkoff.financial.tracker.service.MailService;

@RequiredArgsConstructor
@RequestMapping("/api/mail")
@RestController
public class MailController {

    public final MailService service;

    @GetMapping("send")
    public ResponseEntity<?> sendMail() {
        service.sendToCurrentUser();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
