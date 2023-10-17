package per.khalilov.crudproject.mappers;

import org.springframework.stereotype.Component;
import per.khalilov.crudproject.dto.request.CategoryUpdateRequest;
import per.khalilov.crudproject.dto.response.CategoryResponse;
import per.khalilov.crudproject.models.Category;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .url(category.getUrl())
                .build();
    }

    public Category fromUpdateRequest(CategoryUpdateRequest request) {
        return Category.builder()
                .url(request.getUrl())
                .build();
    }
}
