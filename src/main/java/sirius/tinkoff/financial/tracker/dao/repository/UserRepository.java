package sirius.tinkoff.financial.tracker.dao.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Override
    @CachePut(value = "users", key = "#entity.login")
    <S extends UserEntity> S save(S entity);

    Optional<UserEntity> findByLogin(String login);

    @Override
    @CacheEvict(value = "users", key = "#entity.login")
    void delete(UserEntity entity);
}
