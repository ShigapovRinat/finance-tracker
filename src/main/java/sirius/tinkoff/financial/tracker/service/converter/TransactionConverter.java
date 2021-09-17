package sirius.tinkoff.financial.tracker.service.converter;

import org.springframework.stereotype.Component;
import sirius.tinkoff.financial.tracker.dao.dto.request.TransactionCreateRequest;
import sirius.tinkoff.financial.tracker.dao.dto.response.TransactionGetResponse;
import sirius.tinkoff.financial.tracker.dao.entity.TransactionEntity;

@Component
public class TransactionConverter {
    public TransactionEntity convert(TransactionCreateRequest transactionData) {
        if (transactionData == null) {
            return null;
        } else {
            return new TransactionEntity()
                    .setAmount(transactionData.getAmount())
                    .setExecutionDateLong(transactionData.getExecutionDateLong());
        }
    }

    public TransactionGetResponse convert(TransactionEntity transactionEntity) {
        if (transactionEntity == null) {
            return null;
        } else {
            return new TransactionGetResponse()
                    .setId(transactionEntity.getId())
                    .setAmount(transactionEntity.getAmount())
                    .setCategoryId(transactionEntity.getCategory().getId())
                    .setWalletId(transactionEntity.getWallet().getId())
                    .setExecutionDateLong(transactionEntity.getExecutionDateLong());
        }
    }
}
