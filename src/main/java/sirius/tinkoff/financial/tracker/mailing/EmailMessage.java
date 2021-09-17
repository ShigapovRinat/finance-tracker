package sirius.tinkoff.financial.tracker.mailing;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.List;

@Log4j2
@Setter
@Getter
public class EmailMessage {

    private final List<String> to;
    private final List<String> cc;
    private final String subject;
    private final String body;
    private String emailFrom;
    private JavaMailSender mailSender;

    public EmailMessage(List<String> to,
                        List<String> cc,
                        String subject,
                        String body) {
        this.cc = cc;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public EmailMessage(List<String> to,
                        String subject,
                        String body) {
        this.cc = Collections.emptyList();
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public void send() {
        if (Strings.isBlank(emailFrom)) {
            log.error("Message not sent. \"emailFrom\" undefined");
            return;
        }

        if (mailSender == null) {
            log.error("Message not sent. \"mailSender\" undefined");
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(emailFrom);
            helper.setTo(to.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setCc(cc.toArray(new String[0]));
            message.setContent(body, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error creating email for recipient: {} with subject: {} body: {}", to, subject, body, e);
        } catch (MailException mailException) {
            log.error("Error sending email for recipient: {} with subject: {} body: {}", to, subject, body, mailException);
        }
        log.info("Message \"{}\" sent to {}", message, to);
    }
}
