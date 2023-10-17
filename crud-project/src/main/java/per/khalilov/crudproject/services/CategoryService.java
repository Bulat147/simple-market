package per.khalilov.crudproject.services;

import per.khalilov.crudproject.dto.request.CategoryRequest;
import per.khalilov.crudproject.dto.request.CategoryUpdateRequest;
import per.khalilov.crudproject.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
    List<CategoryResponse> findAll();
    CategoryResponse findById(UUID categoryId);
    CategoryResponse updateById(CategoryUpdateRequest updateRequest, UUID categoryId);
    void deleteById(UUID categoryId);


}
