package sirius.tinkoff.financial.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sirius.tinkoff.financial.tracker.dao.dto.CategoryDto;
import sirius.tinkoff.financial.tracker.dao.dto.request.CategoryCreateRequest;
import sirius.tinkoff.financial.tracker.dao.entity.CategoryEntity;
import sirius.tinkoff.financial.tracker.dao.repository.CategoryRepository;
import sirius.tinkoff.financial.tracker.exception.IllegalOperationException;
import sirius.tinkoff.financial.tracker.exception.OutsideCategoryAccessException;
import sirius.tinkoff.financial.tracker.exception.UnableToFindCategoryException;
import sirius.tinkoff.financial.tracker.model.Session;
import sirius.tinkoff.financial.tracker.service.converter.CategoryConverter;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;
    private final Session session;

    @Transactional
    public CategoryDto createCategory(CategoryCreateRequest categoryDataRequest) {
        CategoryEntity categoryEntity = categoryConverter.convert(categoryDataRequest);
        categoryEntity.setUser(session.getUserEntity());
        categoryRepository.save(categoryEntity);
        log.info("Category with id: " + categoryEntity.getId() + ", name: " + categoryEntity.getName() + " was added");
        return categoryConverter.convert(categoryEntity);
    }

    public CategoryDto changeCategory(CategoryDto categoryDto) {
        CategoryEntity categoryEntity = getCategoryEntityById(categoryDto.getId());
        String previousCategoryEntityString = categoryEntity.getName();

        if (!categoryEntity.getUser().equals(session.getUserEntity())) {
            log.warn("Attempt to change outside category, user ID: " + session.getUserEntity().getId());
            throw new OutsideCategoryAccessException();
        }

        categoryEntity = categoryRepository.save(categoryConverter.convert(categoryDto));
        log.info("Category with id: " + categoryEntity.getId() + ", name: " + previousCategoryEntityString +
                " was changed, new name: " + categoryEntity.getName());
        return categoryConverter.convert(categoryEntity);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        CategoryEntity categoryEntity = getCategoryEntityById(categoryId);
        if (!CollectionUtils.isEmpty(categoryEntity.getTransactions())) {
            throw new IllegalOperationException("Unable to delete category which have not deleted transactions");
        }
        categoryRepository.delete(categoryEntity);
        log.info("Category with id: " + categoryEntity.getId() + ", name: " + categoryEntity.getName() + " was deleted");
    }

    public CategoryDto getCategoryById(Long categoryId) {
        return categoryConverter.convert(getCategoryEntityById(categoryId));
    }

    public List<CategoryDto> getAllUserCategories() {
        return categoryRepository.findAllByUser(session.getUserEntity()).stream()
                .map(categoryConverter::convert)
                .collect(Collectors.toList());
    }

    public CategoryEntity getCategoryEntityById(Long categoryId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(UnableToFindCategoryException::new);
        if (!categoryEntity.getUser().equals(session.getUserEntity())) {
            log.warn("Attempt to get outside category by category ID: " + categoryId + ", user ID: " + session.getUserEntity().getId());
            throw new OutsideCategoryAccessException();
        }
        return categoryEntity;
    }
}
