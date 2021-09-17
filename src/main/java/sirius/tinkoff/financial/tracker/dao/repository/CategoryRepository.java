package sirius.tinkoff.financial.tracker.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sirius.tinkoff.financial.tracker.dao.entity.CategoryEntity;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAllByUser(UserEntity userEntity);
}
