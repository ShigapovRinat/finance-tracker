package sirius.tinkoff.financial.tracker.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;
import sirius.tinkoff.financial.tracker.dao.entity.WalletEntity;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    List<WalletEntity> findAllByUser(UserEntity userEntity);

    void deleteAllByUser(UserEntity userEntity);
}
