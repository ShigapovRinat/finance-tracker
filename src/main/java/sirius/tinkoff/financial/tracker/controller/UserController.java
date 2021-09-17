package sirius.tinkoff.financial.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sirius.tinkoff.financial.tracker.dao.dto.UserDto;
import sirius.tinkoff.financial.tracker.dao.dto.request.UserEditRequest;
import sirius.tinkoff.financial.tracker.dao.dto.response.AllWalletsForUserResponse;
import sirius.tinkoff.financial.tracker.service.CategoryService;
import sirius.tinkoff.financial.tracker.service.UserService;
import sirius.tinkoff.financial.tracker.service.WalletService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users/current")
@AllArgsConstructor
@Setter
@Tag(name = "Пользователи", description = "Позволяет работать с пользователями")
public class UserController {

    private final UserService userService;
    private final WalletService walletService;
    private final CategoryService categoryService;

    @Operation(
            summary = "Метод для получения текущего пользователя",
            description = "Для получения текущего пользователя требуется UID пользователя."
    )
    @GetMapping
    public ResponseEntity<UserDto> getUser() {
        return new ResponseEntity<>(userService.getUser(), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для изменения данных пользователя",
            description = "Для изменения требуется login."
    )
    @PutMapping("/edit")
    public ResponseEntity<UserDto> editUser(@Valid @RequestBody UserEditRequest userEditRequestDto) {
        return new ResponseEntity<>(userService.editUser(userEditRequestDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для удаления пользователя",
            description = "Для удаления пользователя используется login передаваемый в header."
    )
    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        userService.deleteUser();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для получения всех кошельков пользователя",
            description = "Для получения требуется login."
    )
    @GetMapping(value = "/wallets")
    public ResponseEntity<AllWalletsForUserResponse> getWallets() {
        return new ResponseEntity<>(walletService.getAllWallets(), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для получения всех категорий пользователя",
            description = "Для получения требуется login."
    )
    @GetMapping(value = "/categories")
    public ResponseEntity<?> getCategories() {
        return new ResponseEntity<>(categoryService.getAllUserCategories(), HttpStatus.OK);
    }
}
