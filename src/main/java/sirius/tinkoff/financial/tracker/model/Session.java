package sirius.tinkoff.financial.tracker.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;

@Component
@RequestScope
@Getter
@Setter
public class Session {
    private UserEntity userEntity;
}
