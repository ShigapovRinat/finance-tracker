package sirius.tinkoff.financial.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import sirius.tinkoff.financial.tracker.dao.dto.TransactionDto;
import sirius.tinkoff.financial.tracker.dao.dto.request.TransactionCreateRequest;
import sirius.tinkoff.financial.tracker.dao.dto.response.TransactionGetResponse;
import sirius.tinkoff.financial.tracker.dao.entity.CategoryEntity;
import sirius.tinkoff.financial.tracker.dao.entity.TransactionEntity;
import sirius.tinkoff.financial.tracker.dao.entity.WalletEntity;
import sirius.tinkoff.financial.tracker.dao.repository.TransactionRepository;
import sirius.tinkoff.financial.tracker.service.converter.TransactionConverter;

import javax.transaction.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CreatorTransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionConverter transactionConverter;
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final CategoryService categoryService;

    @Transactional
    public TransactionGetResponse createTransaction(TransactionCreateRequest transactionData) {
        CategoryEntity categoryEntity = categoryService.getCategoryEntityById(transactionData.getCategoryId());
        WalletEntity walletEntity = walletService.getWalletEntityById(transactionData.getWalletId());
        walletEntity = walletService.addTransaction(walletEntity.getId(), transactionData.getAmount(), categoryEntity.isIncome());
        TransactionEntity transactionEntity = transactionConverter.convert(transactionData)
                .setWallet(walletEntity)
                .setCategory(categoryEntity);
        return transactionConverter.convert(transactionRepository.save(transactionEntity));
    }

    @Transactional
    public TransactionGetResponse changeTransaction(TransactionDto transactionDto) {
        TransactionEntity transactionEntity = transactionService.getTransactionEntityById(transactionDto.getId());
        CategoryEntity categoryOldEntity = transactionEntity.getCategory();
        CategoryEntity categoryNewEntity = categoryService.getCategoryEntityById(transactionDto.getCategoryId());

        int difference = transactionDto.getAmount() - transactionEntity.getAmount();
        WalletEntity walletEntity;
        if (categoryNewEntity.getId().equals(categoryOldEntity.getId())) {
            walletEntity = walletService
                    .addTransaction(transactionEntity.getWallet().getId(),
                            difference,
                            categoryOldEntity.isIncome());
        } else {
            transactionEntity.setCategory(categoryNewEntity);
            if (categoryNewEntity.isIncome() == categoryOldEntity.isIncome()) {
                walletEntity = walletService
                        .addTransaction(transactionEntity.getWallet().getId(),
                                difference,
                                categoryOldEntity.isIncome());
            } else {
                walletEntity = walletService.changeTransaction(
                        transactionEntity.getWallet().getId(),
                        transactionDto.getAmount(),
                        categoryNewEntity.isIncome());
            }
        }
        transactionEntity
                .setAmount(transactionDto.getAmount())
                .setWallet(walletEntity);
        transactionRepository.save(transactionEntity);
        return transactionConverter.convert(transactionEntity);
    }
}