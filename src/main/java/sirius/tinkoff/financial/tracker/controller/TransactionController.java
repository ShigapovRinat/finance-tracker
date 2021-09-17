package sirius.tinkoff.financial.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sirius.tinkoff.financial.tracker.dao.dto.TransactionDto;
import sirius.tinkoff.financial.tracker.dao.dto.request.TransactionCreateRequest;
import sirius.tinkoff.financial.tracker.dao.dto.response.TransactionGetResponse;
import sirius.tinkoff.financial.tracker.service.CreatorTransactionService;
import sirius.tinkoff.financial.tracker.service.TransactionService;

import javax.validation.Valid;
import javax.ws.rs.Produces;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Операции", description = "Позволяет работать с операциями")
public class TransactionController {

    private final TransactionService transactionService;
    private final CreatorTransactionService creatorTransactionService;

    @Operation(
            summary = "Метод для создания операции",
            description = "Для создания операции требуется ID категории, сумму операции, ID кошелька."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionCreateRequest transactionData) {
        return new ResponseEntity<>(creatorTransactionService.createTransaction(transactionData), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для изменения операции",
            description = "Для изменения операции требуется указать ID операции, которая будет изменена. А так же новое наименование ID категории и сумму операции."
    )
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        return new ResponseEntity<>(creatorTransactionService.changeTransaction(transactionDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для удаления операции",
            description = "Для удаления требуется указать ID операции."
    )
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable("id") Long id) {
        transactionService.deleteTransaction(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для получения операции по ID",
            description = "Для получения операции требуется указать её ID."
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<TransactionGetResponse> getTransactionById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(transactionService.getTransactionById(id), HttpStatus.OK);
    }
}
