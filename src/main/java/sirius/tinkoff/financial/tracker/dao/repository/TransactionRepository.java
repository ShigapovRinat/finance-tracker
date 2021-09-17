package sirius.tinkoff.financial.tracker.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sirius.tinkoff.financial.tracker.dao.entity.TransactionEntity;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findAllByWalletId(Long id);

    List<TransactionEntity> findAllByWalletIdAndExecutionDateLongAfter(Long id, Long afterDate);
}
