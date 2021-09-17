package sirius.tinkoff.financial.tracker.service.converter;

import org.springframework.stereotype.Component;
import sirius.tinkoff.financial.tracker.dao.dto.UserDto;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;

@Component
public class UserConverter {

    public UserEntity convert(UserDto userDto) {
        return new UserEntity()
                .setLogin(userDto.getLogin())
                .setName(userDto.getName())
                .setSurname(userDto.getSurname())
                .setPhoneNumber(userDto.getPhoneNumber());
    }

    public UserDto convert(UserEntity userEntity) {
        return new UserDto()
                .setLogin(userEntity.getLogin())
                .setName(userEntity.getName())
                .setSurname(userEntity.getSurname())
                .setPhoneNumber(userEntity.getPhoneNumber());
    }
}
