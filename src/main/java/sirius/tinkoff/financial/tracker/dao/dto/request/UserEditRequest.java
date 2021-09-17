package sirius.tinkoff.financial.tracker.dao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Запрос на изменение пользователя")
public class UserEditRequest {

    @NotBlank
    @Length(max = 30)
    @Schema(description = "Имя пользователя. Максимальная длина: 30 символов")
    private String name;

    @NotBlank
    @Length(max = 30)
    @Schema(description = "Фамилия пользователя. Максимальная длина: 30 символов")
    private String surname;
}
