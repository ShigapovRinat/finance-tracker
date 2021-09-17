package sirius.tinkoff.financial.tracker.dao.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Запрос на создание категории")
public class CategoryCreateRequest {

    @NotBlank
    @Length(max = 30)
    @Schema(description = "Наименование категории. Максимальная длина: 30 символов")
    String name;

    @NotNull
    @Schema(description = "ID иконки, привязанной к данной категории")
    Long pictureId;

    @JsonProperty("income")
    @Schema(description = "Тип категории: true - категория относится к доходу\n" +
            "false - категория относится к расходу")
    boolean isIncome;
}
