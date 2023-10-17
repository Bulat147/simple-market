package per.khalilov.crudproject.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import per.khalilov.crudproject.dto.request.CategoryRequest;
import per.khalilov.crudproject.dto.request.CategoryUpdateRequest;
import per.khalilov.crudproject.dto.response.CategoryResponse;
import per.khalilov.crudproject.dto.response.ProductResponse;
import per.khalilov.crudproject.exceptions.EntityAlreadyExistsException;
import per.khalilov.crudproject.exceptions.NoSuchEntityException;
import per.khalilov.crudproject.mappers.CategoryMapper;
import per.khalilov.crudproject.mappers.ProductMapper;
import per.khalilov.crudproject.models.Category;
import per.khalilov.crudproject.repositories.CategoryRepository;
import per.khalilov.crudproject.repositories.ProductRepository;
import per.khalilov.crudproject.services.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static per.khalilov.crudproject.constants.CategoryConstants.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category(request.getName());
        if (categoryRepository.findByUrl(category.getUrl()).isPresent()) {
            throw new EntityAlreadyExistsException(CATEGORY_ALREADY_EXISTS_EXCEPTION_MESSAGE);
        }
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryResponse response = categoryMapper.toResponse(category);
                    List<ProductResponse> products = productRepository.findAll().stream()
                            .filter(product -> product.getCategoryId().equals(category.getId()))
                            .map(productMapper::toProductResponse)
                            .toList();
                    response.setProducts(products);
                    return response;
                }).toList();
    }

    @Override
    public CategoryResponse findById(UUID categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            // Подумал что 400 и throw не нужен, ведь это не ошибка приложения, а просто не найдено
            // при поиске пользователем
            return null;
        }
        Category category = categoryOptional.get();
        CategoryResponse response = categoryMapper.toResponse(category);
        List<ProductResponse> productResponses = productRepository.findAll().stream()
                .filter(product -> product.getCategoryId().equals(categoryId))
                .map(productMapper::toProductResponse)
                .toList();
        response.setProducts(productResponses);
        return response;
    }

    @Override
    public CategoryResponse updateById(CategoryUpdateRequest updateRequest, UUID categoryId) {
        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new NoSuchEntityException(NO_SUCH_CATEGORY_EXCEPTION_MESSAGE);
        }
        Category categoryByUrl = categoryRepository.findByUrl(updateRequest.getUrl()).orElse(null);
        if (categoryByUrl != null && categoryByUrl.getId().equals(categoryId)) {
            return categoryMapper.toResponse(categoryByUrl);
        }
        if (categoryByUrl != null) {
            throw new EntityAlreadyExistsException(CATEGORY_ALREADY_EXISTS_EXCEPTION_MESSAGE);
        }
        Category category = categoryMapper.fromUpdateRequest(updateRequest);
        category.setId(categoryId);
        return categoryMapper.toResponse(categoryRepository.update(category));
    }

    @Override
    public void deleteById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null && !category.getUrl().equals(DEFAULT_CATEGORY_URL)) {
            categoryRepository.deleteById(categoryId);
        }
    }

}
