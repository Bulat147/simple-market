package per.khalilov.crudproject.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import per.khalilov.crudproject.models.Category;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static per.khalilov.crudproject.constants.CategoryConstants.DEFAULT_CATEGORY_URL;

@Testcontainers
@SpringBootTest
@TestPropertySource(properties = {"spring.config.location=classpath:application-testcontainers.yaml"})
public class CategoryRepositoryJdbcImplIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:12.16"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void when_findAll_then_return_list_contains_default_category_by_default() {
        List<String> urls = categoryRepository.findAll().stream()
                .map(Category::getUrl)
                .toList();
        assertThat(urls.contains(DEFAULT_CATEGORY_URL)).isTrue();
    }

    @Test
    public void when_save_then_return_same_category() {
        Category category = Category.builder()
                .id(UUID.randomUUID())
                .url("one")
                .build();
        assertThat(categoryRepository.save(category)).isEqualTo(category);
    }

    @Test
    public void when_save_then_truly_save_in_db() {
        UUID id = UUID.randomUUID();
        Category category = Category.builder()
                .id(id)
                .url("two")
                .build();
        assertThat(categoryRepository.findById(id).orElse(null)).isEqualTo(null);
        categoryRepository.save(category);
        assertThat(categoryRepository.findById(id).orElse(null)).isEqualTo(category);
    }

    @Test
    public void when_update_then_truly_update_in_db() {
        UUID id = UUID.randomUUID();
        Category category = Category.builder()
                .id(id)
                .url("three")
                .build();
        categoryRepository.save(category);
        category.setUrl("new");
        categoryRepository.update(category);
        assertThat(categoryRepository.findById(id).orElse(null)).isEqualTo(category);
    }

    @Test
    public void when_delete_then_truly_delete_in_db() {
        UUID id = UUID.randomUUID();
        Category category = Category.builder()
                .id(id)
                .url("four")
                .build();
        categoryRepository.save(category);
        assertThat(categoryRepository.findById(id).orElse(null)).isEqualTo(category);
        categoryRepository.deleteById(id);
        assertThat(categoryRepository.findById(id).orElse(null)).isEqualTo(null);
    }

    @Test
    public void when_findById_and_findByUrl_on_same_object_then_return_truly_same_category() {
        Category category = Category.builder()
                .id(UUID.randomUUID())
                .url("five")
                .build();
        categoryRepository.save(category);
        assertThat(categoryRepository.findById(category.getId()).orElse(null))
                .isEqualTo(categoryRepository.findByUrl(category.getUrl()).orElse(null))
                .isEqualTo(category);
    }
}
