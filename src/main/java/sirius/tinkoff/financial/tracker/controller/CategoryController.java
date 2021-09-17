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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sirius.tinkoff.financial.tracker.dao.dto.request.CategoryCreateRequest;
import sirius.tinkoff.financial.tracker.service.CategoryService;

import javax.validation.Valid;
import javax.ws.rs.Produces;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Категории", description = "Позволяет работать с категориями")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Метод для создания категории",
            description = "Для создания категории требуется указать наименование категории, ID иконки, тип категории."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryCreateRequest categoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для получения категории по ID",
            description = "Для получения категории требуется указать ID категории."
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Метод для удаления категории",
            description = "Для удаления требуется указать ID категории."
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}