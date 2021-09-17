package sirius.tinkoff.financial.tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;
import sirius.tinkoff.financial.tracker.dao.repository.UserRepository;
import sirius.tinkoff.financial.tracker.mailing.BodyGenerator;
import sirius.tinkoff.financial.tracker.mailing.EmailMessage;
import sirius.tinkoff.financial.tracker.model.Session;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {

    private final static String SUBJECT = "Кошелёк: отчёт о финансовых активностях.";

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final Session session;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendMessage(EmailMessage emailMessage) {
        emailMessage.setMailSender(mailSender);
        emailMessage.setEmailFrom(emailFrom);
        emailMessage.send();
    }

    @Transactional
    public void sendAllMessages() {
        for (UserEntity user : userRepository.findAll()) {
            sendToUser(user);
        }
    }

    @Transactional
    public void sendToUser(UserEntity userEntity) {
        String body = new BodyGenerator().generateHtml(userEntity);
        EmailMessage message = new EmailMessage(List.of(userEntity.getLogin()), Collections.emptyList(), SUBJECT, body);
        sendMessage(message);
    }

    public void sendToCurrentUser() {
        UserEntity userEntity = userRepository.getById(session.getUserEntity().getId());
        sendToUser(userEntity);
    }
}
