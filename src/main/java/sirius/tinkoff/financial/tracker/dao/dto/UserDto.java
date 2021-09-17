package sirius.tinkoff.financial.tracker.dao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import sirius.tinkoff.financial.tracker.validation.annotation.ValidPhoneNumber;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Пользователь.")
public class UserDto {

    @Schema(description = "Логин пользователя")
    private String login;

    @Schema(description = "Имя пользователя")
    private String name;

    @Schema(description = "Фамилия пользователя")
    private String surname;

    @ValidPhoneNumber
    @Schema(description = "Мобильный телефон, имеет формат 8XXXXXXXXXX")
    private Long phoneNumber;

}
