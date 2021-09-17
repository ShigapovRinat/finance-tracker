package sirius.tinkoff.financial.tracker.dao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Запрос на создание операции")
public class TransactionCreateRequest {

    @NotNull
    @Range(min = 0)
    @Schema(description = "Сумма операции. Всегда положительна")
    private Integer amount;

    @NotNull
    @Schema(description = "ID категории, к которой относится операция")
    private Long categoryId;

    @NotNull
    @Schema(description = "ID кошелька")
    private Long walletId;

    @NotNull
    @Schema(description = "Дата выполнения транзакции, задаваемая пользователем")
    private Long executionDateLong;
}
