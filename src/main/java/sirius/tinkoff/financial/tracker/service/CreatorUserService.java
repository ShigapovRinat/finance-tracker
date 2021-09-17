package sirius.tinkoff.financial.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;
import sirius.tinkoff.financial.tracker.dao.repository.CategoryRepository;
import sirius.tinkoff.financial.tracker.dao.repository.UserRepository;

import javax.transaction.Transactional;

import static sirius.tinkoff.financial.tracker.util.CreatorDefaultCategoryUtil.createDefaultCategories;

@Log4j2
@Service
@Setter
@RequiredArgsConstructor
public class CreatorUserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Cacheable("users")
    public UserEntity createUser(UserEntity userEntity) {
        userRepository.save(userEntity);
        categoryRepository.saveAll(createDefaultCategories(userEntity));
        log.info("User with id: " + userEntity.getId() + ", login: " + userEntity.getLogin() + "was added");
        return userEntity;
    }

}
