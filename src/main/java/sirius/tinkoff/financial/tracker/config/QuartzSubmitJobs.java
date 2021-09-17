package sirius.tinkoff.financial.tracker.config;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import sirius.tinkoff.financial.tracker.job.EmailJob;

@Configuration
public class QuartzSubmitJobs {

    @Bean(name = "email")
    public JobDetailFactoryBean jobEmail() {
        return QuartzConfig.createJobDetail(EmailJob.class, "Email Job");
    }

    @Bean(name = "emailTrigger")
    public CronTriggerFactoryBean triggerEmail(@Qualifier("email") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, "0 0/10 * ? * * *", "Email Trigger");
    }
}
