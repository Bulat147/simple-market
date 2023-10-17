package per.khalilov.crudproject.repositories;


import per.khalilov.crudproject.models.Category;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static per.khalilov.crudproject.constants.CategoryConstants.DEFAULT_CATEGORY_URL;

public interface CategoryRepository {
    @PostConstruct
    default void createDefaultCategory() {
        Category category = findByUrl(DEFAULT_CATEGORY_URL).orElse(null);
        if (category == null) {
            category = Category.builder()
                    .url(DEFAULT_CATEGORY_URL)
                    .id(UUID.randomUUID())
                    .build();
            save(category);
        }
    }
    Category save(Category category);
    Category update(Category category);
    void deleteById(UUID categoryId);
    List<Category> findAll();
    Optional<Category> findById(UUID categoryId);
    Optional<Category> findByUrl(String categoryUrl);
}
