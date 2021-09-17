package sirius.tinkoff.financial.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import sirius.tinkoff.financial.tracker.dao.dto.WalletDto;
import sirius.tinkoff.financial.tracker.dao.dto.request.WalletCreateRequest;
import sirius.tinkoff.financial.tracker.dao.dto.response.AllWalletsForUserResponse;
import sirius.tinkoff.financial.tracker.dao.dto.response.WalletGetResponse;
import sirius.tinkoff.financial.tracker.dao.entity.WalletEntity;
import sirius.tinkoff.financial.tracker.dao.repository.WalletRepository;
import sirius.tinkoff.financial.tracker.exception.InsufficientFundsException;
import sirius.tinkoff.financial.tracker.exception.OutsideWalletAccessException;
import sirius.tinkoff.financial.tracker.exception.UnableToFindWalletException;
import sirius.tinkoff.financial.tracker.model.Session;
import sirius.tinkoff.financial.tracker.service.converter.WalletConverter;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletConverter walletConverter;
    private final Session session;

    public WalletGetResponse addWallet(WalletCreateRequest wallet) {
        WalletEntity walletEntity = walletConverter.converterCreate(wallet);
        walletEntity.setUser(session.getUserEntity());
        walletEntity = walletRepository.save(walletEntity);
        log.info("Wallet with id: " + walletEntity.getId() + " was added");
        return walletConverter.converter(walletEntity);
    }

    public void deleteWallet(Long id) {
        WalletEntity walletEntity = walletRepository.findById(id)
                .orElseThrow(UnableToFindWalletException::new);
        if (!session.getUserEntity().equals(walletEntity.getUser())) {
            log.warn("Attempt to gain access to outside wallet, user ID: " + session.getUserEntity().getId());
            throw new OutsideWalletAccessException();
        }
        walletRepository.delete(walletEntity);
        log.info("Wallet with id: " + walletEntity.getId() + " was deleted");
    }

    @Transactional
    public WalletGetResponse editWallet(WalletDto dto) {
        WalletEntity walletEntity = getWalletEntityById(dto.getId());
        if (!session.getUserEntity().equals(walletEntity.getUser())) {
            log.warn("Attempt to gain access to outside wallet, user ID: " + session.getUserEntity().getId());
            throw new OutsideWalletAccessException();
        }
        walletEntity
                .setLimit(dto.getLimit())
                .setName(dto.getName());
        log.info("Wallet with id: " + walletEntity.getId() + " was edited");
        return walletConverter.converter(walletRepository.save(walletEntity));
    }

    public WalletGetResponse getWalletById(Long id) {
        Optional<WalletEntity> walletEntityOpt = walletRepository.findById(id);
        WalletEntity walletEntity = walletEntityOpt.orElseThrow(UnableToFindWalletException::new);
        if (!session.getUserEntity().equals(walletEntity.getUser())) {
            log.warn("Attempt to gain access to outside wallet, user ID: " + session.getUserEntity().getId());
            throw new OutsideWalletAccessException();
        }
        return walletConverter.converter(walletEntity);
    }

    public WalletEntity getWalletEntityById(Long id) {
        WalletEntity walletEntity = walletRepository.findById(id).orElseThrow(UnableToFindWalletException::new);
        if (!session.getUserEntity().equals(walletEntity.getUser())) {
            log.warn("Attempt to gain access to outside wallet, user ID: " + session.getUserEntity().getId());
            throw new OutsideWalletAccessException();
        }
        return walletEntity;
    }

    public AllWalletsForUserResponse getAllWallets() {
        List<WalletGetResponse> walletDtoList = walletRepository.findAllByUser(session.getUserEntity()).stream()
                .map(walletConverter::converter)
                .collect(Collectors.toList());
        int allProfit = walletDtoList.stream().mapToInt(WalletGetResponse::getProfit).sum();
        int allConsumption = walletDtoList.stream().mapToInt(WalletGetResponse::getConsumption).sum();
        return new AllWalletsForUserResponse(walletDtoList, allProfit, allConsumption);
    }

    @Transactional
    public WalletEntity addTransaction(Long id, Integer amount, boolean isIncome) {
        WalletEntity walletEntity = walletRepository.findById(id).orElseThrow(UnableToFindWalletException::new);
        if (isIncome) {
            if (walletEntity.getProfit() + amount < walletEntity.getConsumption()) {
                throw new InsufficientFundsException();
            }
            walletEntity.setProfit(walletEntity.getProfit() + amount);
        } else {
            if (walletEntity.getProfit() < walletEntity.getConsumption() + amount) {
                throw new InsufficientFundsException();
            }
            walletEntity.setConsumption(walletEntity.getConsumption() + amount);
        }
        return walletRepository.save(walletEntity);
    }

    @Transactional
    public WalletEntity changeTransaction(Long id, Integer amount, boolean isIncome) {
        WalletEntity walletEntity = getWalletEntityById(id);
        if (!isIncome && (walletEntity.getProfit() - amount < walletEntity.getConsumption() + amount)) {
            throw new InsufficientFundsException();
        }
        addTransaction(id, -amount, !isIncome);
        return addTransaction(id, amount, isIncome);
    }

    @Transactional
    public WalletEntity deleteTransaction(Long walletId, Integer amount, boolean isIncome) {
        WalletEntity walletEntity = getWalletEntityById(walletId);
        if (isIncome) {
            if (walletEntity.getProfit() - amount < walletEntity.getConsumption()) {
                throw new InsufficientFundsException();
            }
            walletEntity.setProfit(walletEntity.getProfit() - amount);
        } else {
            walletEntity.setConsumption(walletEntity.getConsumption() - amount);
        }
        return walletRepository.save(walletEntity);
    }
}
