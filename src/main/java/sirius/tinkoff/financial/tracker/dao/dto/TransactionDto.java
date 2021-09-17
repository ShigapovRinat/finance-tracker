package sirius.tinkoff.financial.tracker.dao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "Операция.")
public class TransactionDto {

    @Schema(description = "ID операции.")
    private Long id; // toDo UID операции, пока что из db

    @NotNull
    @Range(min = 0)
    @Schema(description = "Сумма операции. Всегда положительна")
    private Integer amount;

    @NotNull
    @Schema(description = "ID категории, к которой относится операция")
    private Long categoryId;

    @NotNull
    @Schema(description = "Время выполнения транзакции, выражается через UNIX-время")
    private Long executionDateLong;
}
