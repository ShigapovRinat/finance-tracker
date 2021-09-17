package sirius.tinkoff.financial.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
import sirius.tinkoff.financial.tracker.dao.dto.WalletDto;
import sirius.tinkoff.financial.tracker.dao.dto.request.WalletCreateRequest;
import sirius.tinkoff.financial.tracker.dao.dto.response.TransactionGetResponse;
import sirius.tinkoff.financial.tracker.dao.dto.response.WalletGetResponse;
import sirius.tinkoff.financial.tracker.service.TransactionService;
import sirius.tinkoff.financial.tracker.service.WalletService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@AllArgsConstructor
@Tag(name = "Кошельки", description = "Позволяет работать с кошельками")
public class WalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;

    @Operation(
            summary = "Метод для создания кошелька",
            description = "Для получения кошелька требуется указать имя кошелька, лимит и ID пользователя (Лимит по умолчанию не установлен)."
    )
    @PostMapping
    public ResponseEntity<?> createWallet(@Valid @RequestBody WalletCreateRequest wallet) {
        return new ResponseEntity<>(walletService.addWallet(wallet), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для изменения кошелька",
            description = "Для изменения кошелька требуется указать ID кошелька, который будет изменен. А так же новое наименование кошелька и лимит."
    )
    @PutMapping
    public ResponseEntity<?> editWallet(@Valid @RequestBody WalletDto dto) {
        return new ResponseEntity<>(walletService.editWallet(dto), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для удаления кошелька",
            description = "Для удаления кошелька требуется указать его ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWallet(@PathVariable("id") Long id) {
        walletService.deleteWallet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для получения кошелька",
            description = "Для получения кошелька требуется указать его ID."
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletGetResponse> getWallet(@PathVariable("id") Long id) {
        return new ResponseEntity<>(walletService.getWalletById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для получение транзакций для определенного кошелька",
            description = "Требуется указать его ID."
    )
    @GetMapping(value = "/{id}/transactions")
    public ResponseEntity<List<TransactionGetResponse>> getTransactionsByWalletId(@PathVariable("id") Long walletId) {
        return new ResponseEntity<>(transactionService.getAllTransactionsByWalletId(walletId, null), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для получение транзакций для определенного кошелька с определённого времени.",
            description = "Требуется указать его ID и время, начиная с которого требуется учитывать транзакции."
    )
    @GetMapping(value = "/{id}/transactions/{afterDate}")
    public ResponseEntity<List<TransactionGetResponse>> getTransactionsByWalletIdAfterDate(@PathVariable("id") Long walletId, @PathVariable Long afterDate) {
        return new ResponseEntity<>(transactionService.getAllTransactionsByWalletId(walletId, afterDate), HttpStatus.OK);
    }
}
