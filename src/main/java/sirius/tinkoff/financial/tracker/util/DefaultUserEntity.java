package sirius.tinkoff.financial.tracker.util;

import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;

public class DefaultUserEntity {
    public static UserEntity get() {
        return new UserEntity();
    }
}
