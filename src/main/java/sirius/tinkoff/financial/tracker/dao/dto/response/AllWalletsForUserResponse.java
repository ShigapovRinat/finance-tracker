package sirius.tinkoff.financial.tracker.dao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class AllWalletsForUserResponse {
    @Schema(description = "Список всех кошельков")
    private List<WalletGetResponse> walletDtoList;

    @Schema(description = "Общий доход")
    private Integer allProfit;

    @Schema(description = "Общий расход")
    private Integer allConsumption;
}
