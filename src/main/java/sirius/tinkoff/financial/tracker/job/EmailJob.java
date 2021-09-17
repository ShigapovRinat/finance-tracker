package sirius.tinkoff.financial.tracker.job;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sirius.tinkoff.financial.tracker.service.MailService;

@Log4j2
@Component
@NoArgsConstructor
public class EmailJob implements Job {

    @Autowired
    private MailService mailService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
        mailService.sendAllMessages();
        log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}