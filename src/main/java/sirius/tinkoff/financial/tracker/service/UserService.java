package sirius.tinkoff.financial.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import sirius.tinkoff.financial.tracker.dao.dto.UserDto;
import sirius.tinkoff.financial.tracker.dao.dto.request.UserEditRequest;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;
import sirius.tinkoff.financial.tracker.dao.repository.UserRepository;
import sirius.tinkoff.financial.tracker.dao.repository.WalletRepository;
import sirius.tinkoff.financial.tracker.exception.UnableToFindUserException;
import sirius.tinkoff.financial.tracker.model.Session;
import sirius.tinkoff.financial.tracker.service.converter.UserConverter;

import javax.transaction.Transactional;
import java.util.Optional;

@Log4j2
@Service
@Setter
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final WalletRepository walletRepository;
    private final Session session;

    public UserDto getUser() {
        return userConverter.convert(session.getUserEntity());
    }

    @Transactional
    public UserDto editUser(UserEditRequest userEditRequest) {
        UserEntity userEntity = getUserEntity();
        userEntity.setName(userEditRequest.getName())
                .setSurname(userEditRequest.getSurname());
        userEntity = userRepository.save(userEntity);
        log.info("User with id: " + userEntity.getId() + ", login: " + userEntity.getLogin() + " was edited");
        return userConverter.convert(userEntity);
    }

    @Transactional
    public void deleteUser() {
        UserEntity userEntity = getUserEntity();
        walletRepository.deleteAllByUser(session.getUserEntity());
        userRepository.delete(userEntity);
        log.info("User with id: " + userEntity.getId() + ", login: " + userEntity.getLogin() + " was deleted");
    }

    public UserEntity getUserEntity() {
        return userRepository.findById(session.getUserEntity().getId()).orElseThrow(UnableToFindUserException::new);
    }

    @Cacheable(value = "users", key = "#login")
    public UserEntity getUserEntityByLogin(String login) {
        Optional<UserEntity> userEntityOpt = userRepository.findByLogin(login);
        return userEntityOpt.orElseThrow(UnableToFindUserException::new);
    }
}
