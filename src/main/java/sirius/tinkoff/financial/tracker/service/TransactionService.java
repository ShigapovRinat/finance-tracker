package sirius.tinkoff.financial.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import sirius.tinkoff.financial.tracker.dao.dto.response.TransactionGetResponse;
import sirius.tinkoff.financial.tracker.dao.entity.TransactionEntity;
import sirius.tinkoff.financial.tracker.dao.repository.TransactionRepository;
import sirius.tinkoff.financial.tracker.exception.OutsideTransactionAccessException;
import sirius.tinkoff.financial.tracker.exception.UnableToFindTransactionException;
import sirius.tinkoff.financial.tracker.model.Session;
import sirius.tinkoff.financial.tracker.service.converter.TransactionConverter;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionConverter transactionConverter;
    private final WalletService walletService;
    private final Session session;

    public void deleteTransaction(Long transactionId) {
        TransactionEntity transactionEntity = transactionRepository.findById(transactionId)
                .orElseThrow(UnableToFindTransactionException::new);
        if (!transactionEntity.getWallet().getUser().equals(session.getUserEntity())) {
            log.warn("Attempt to delete outside transaction, user ID: " + session.getUserEntity().getId());
            throw new OutsideTransactionAccessException();
        }
        walletService.deleteTransaction(transactionEntity.getWallet().getId(), transactionEntity.getAmount(), transactionEntity.getCategory().isIncome());
        transactionRepository.delete(transactionEntity);
        log.info("Transaction with id: " + transactionEntity.getId() + ", amount: " + transactionEntity.getAmount() + " was added");
    }

    public TransactionGetResponse getTransactionById(Long id) {
        return transactionConverter.convert(getTransactionEntityById(id));
    }

    public List<TransactionGetResponse> getAllTransactionsByWalletId(Long id, Long afterDate) {
        walletService.getWalletEntityById(id);
        List<TransactionEntity> transactionEntities = (afterDate == null) ?
                transactionRepository.findAllByWalletId(id) :
                transactionRepository.findAllByWalletIdAndExecutionDateLongAfter(id, afterDate);
        return transactionEntities.stream()
                .map(transactionConverter::convert)
                .collect(Collectors.toList());
    }

    public TransactionEntity getTransactionEntityById(Long transactionId) {
        TransactionEntity transactionEntity = transactionRepository.findById(transactionId)
                .orElseThrow(UnableToFindTransactionException::new);
        if (!transactionEntity.getWallet().getUser().equals(session.getUserEntity())) {
            log.warn("Attempt to get outside transaction by transaction ID: " + transactionEntity.getId() + ", user ID: " + session.getUserEntity().getId());
            throw new OutsideTransactionAccessException();
        }
        return transactionEntity;
    }
}