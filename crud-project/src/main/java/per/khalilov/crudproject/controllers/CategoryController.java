package per.khalilov.crudproject.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import per.khalilov.crudproject.api.CategoryApi;
import per.khalilov.crudproject.dto.request.CategoryRequest;
import per.khalilov.crudproject.dto.request.CategoryUpdateRequest;
import per.khalilov.crudproject.dto.response.CategoryResponse;
import per.khalilov.crudproject.services.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        return categoryService.create(request);
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @Override
    public CategoryResponse findById(UUID categoryId) {
        return categoryService.findById(categoryId);
    }

    @Override
    public CategoryResponse updateById(CategoryUpdateRequest updateRequest, UUID categoryId) {
        return categoryService.updateById(updateRequest, categoryId);
    }

    @Override
    public void deleteById(UUID categoryId) {
        categoryService.deleteById(categoryId);
    }
}
