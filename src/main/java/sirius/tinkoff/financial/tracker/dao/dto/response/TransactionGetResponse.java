package sirius.tinkoff.financial.tracker.dao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TransactionGetResponse {
    @Schema(description = "ID операции")
    private Long id; // toDo UID операции, пока что из db

    @Schema(description = "Сумма операции")
    private Integer amount;

    @Schema(description = "ID категории, к которой относится операция")
    private Long categoryId;

    @Schema(description = "ID кошелька")
    private Long walletId;

    @Schema(description = "Время выполнения транзакции, выражается через UNIX-время")
    private Long executionDateLong;
}
