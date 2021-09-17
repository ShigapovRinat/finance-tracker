package sirius.tinkoff.financial.tracker.service.converter;

import org.springframework.stereotype.Component;
import sirius.tinkoff.financial.tracker.dao.dto.CategoryDto;
import sirius.tinkoff.financial.tracker.dao.dto.request.CategoryCreateRequest;
import sirius.tinkoff.financial.tracker.dao.entity.CategoryEntity;

@Component
public class CategoryConverter {
    public CategoryDto convert(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        } else {
            return CategoryDto.builder()
                    .id(categoryEntity.getId())
                    .name(categoryEntity.getName())
                    .pictureId(categoryEntity.getPictureId())
                    .isIncome(categoryEntity.isIncome()).build();
        }
    }

    public CategoryEntity convert(CategoryCreateRequest categoryDataRequest) {
        if (categoryDataRequest == null) {
            return null;
        } else {
            return new CategoryEntity()
                    .setName(categoryDataRequest.getName())
                    .setPictureId(categoryDataRequest.getPictureId())
                    .setIncome(categoryDataRequest.isIncome());

        }
    }
}
