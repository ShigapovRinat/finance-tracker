package sirius.tinkoff.financial.tracker.dao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Ответ на запрос о кошельке")
public class WalletGetResponse {
    @Schema(description = "ID кошелька")
    private Long id;

    @Schema(description = "Имя кошелька")
    private String name;

    @Schema(description = "Установленный лимит кошелька")
    private Integer limit;

    @Schema(description = "Доход кошелька")
    private Integer profit;

    @Schema(description = "Расход кошелька")
    private Integer consumption;
}
