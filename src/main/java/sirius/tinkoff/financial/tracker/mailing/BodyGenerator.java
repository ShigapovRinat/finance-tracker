package sirius.tinkoff.financial.tracker.mailing;

import lombok.extern.log4j.Log4j2;
import org.apache.cxf.helpers.IOUtils;
import org.springframework.core.io.ClassPathResource;
import sirius.tinkoff.financial.tracker.dao.entity.TransactionEntity;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;
import sirius.tinkoff.financial.tracker.dao.entity.WalletEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Log4j2
public class BodyGenerator {

    private static final String templateHeader;
    private static final String bodyTemplate;
    private static final String walletHeaderTemplate;
    private static final String walletBottomTemplate;
    private static final String transactionTemplate;
    private static final String bottomTemplate;
    private static final String ENCODING = StandardCharsets.UTF_8.name();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    static {
        ClassPathResource headerConfFile = new ClassPathResource("mail_templates/header.template");
        ClassPathResource bodyConfFile = new ClassPathResource("mail_templates/body.template");
        ClassPathResource walletHeaderConfFile = new ClassPathResource("mail_templates/walletHeader.template");
        ClassPathResource walletBottomConfFile = new ClassPathResource("mail_templates/walletBottom.template");
        ClassPathResource transactionConFile = new ClassPathResource("mail_templates/transaction.template");
        ClassPathResource bottomConFile = new ClassPathResource("mail_templates/bottom.template");
        try {
            bodyTemplate = IOUtils.toString(bodyConfFile.getInputStream(), ENCODING);
            templateHeader = IOUtils.toString(headerConfFile.getInputStream(), ENCODING);
            walletHeaderTemplate = IOUtils.toString(walletHeaderConfFile.getInputStream(), ENCODING);
            walletBottomTemplate = IOUtils.toString(walletBottomConfFile.getInputStream(), ENCODING);
            transactionTemplate = IOUtils.toString(transactionConFile.getInputStream(), ENCODING);
            bottomTemplate = IOUtils.toString(bottomConFile.getInputStream(), ENCODING);
        } catch (IOException e) {
            log.error("Error while reading html templates");
            throw new RuntimeException("Unable to get message");
        }
    }


    public String generateHtml(UserEntity user) {
        StringBuilder bodySb = new StringBuilder();
        int balance = 0;
        for (WalletEntity wallet : user.getWallets()) {
            for (TransactionEntity transaction : wallet.getTransactions()) {
                balance += transaction.getAmount() * (transaction.getCategory().isIncome() ? 1 : -1);
            }
            bodySb.append(getWallet(wallet));
        }
        String body = MessageFormat.format(
                bodyTemplate,
                user.getName().equals("Unknown") ? "" : ", " + user.getName(),
                dateFormatter.format(LocalDateTime.now()),
                balance);
        return templateHeader + body + bodySb + bottomTemplate;
    }

    private String getTransaction(TransactionEntity transactionEntity) {
        return MessageFormat.format(
                transactionTemplate,
                timeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(transactionEntity.getExecutionDateLong()),
                        TimeZone.getDefault().toZoneId())),
                transactionEntity.getCategory().getName(),
                (transactionEntity.getCategory().isIncome() ? "+" : "-") + transactionEntity.getAmount()
        );
    }

    private String getWallet(WalletEntity walletEntity) {
        StringBuilder walletBodySb = new StringBuilder();
        int lastDay = 0;
        List<TransactionEntity> transactionEntities = getTransactionEntities(walletEntity);
        for (TransactionEntity transaction : transactionEntities) {
            lastDay += transaction.getAmount() * (transaction.getCategory().isIncome() ? 1 : -1);
            walletBodySb.append(getTransaction(transaction));
        }
        String walletHeader = MessageFormat.format(walletHeaderTemplate, walletEntity.getName());
        String walletBottom = MessageFormat.format(
                walletBottomTemplate,
                lastDay,
                walletEntity.getProfit() - walletEntity.getConsumption()
        );
        return walletHeader + walletBodySb + walletBottom;
    }

    private List<TransactionEntity> getTransactionEntities(WalletEntity walletEntity) {
        return walletEntity
                .getTransactions()
                .stream().filter(t -> t.getExecutionDateLong() > Calendar.getInstance().getTimeInMillis() - 86400000)
                .collect(Collectors.toList());
    }
}
