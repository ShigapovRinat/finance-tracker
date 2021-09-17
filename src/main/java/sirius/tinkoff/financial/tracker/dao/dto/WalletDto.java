package sirius.tinkoff.financial.tracker.dao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Schema(description = "Кошелёк")
public class WalletDto {

    @Schema(description = "ID кошелька")
    private Long id;

    @NotBlank
    @Length(max = 30)
    @Schema(description = "Имя кошелька. Максимальная длина: 30 символов")
    private String name;

    @Range(min = 1)
    @Schema(description = "Установленный лимит кошелька. Минимальный лимит: 1")
    private Integer limit;
}
