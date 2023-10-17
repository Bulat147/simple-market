package per.khalilov.crudproject.mappers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import per.khalilov.crudproject.dto.request.CategoryUpdateRequest;
import per.khalilov.crudproject.dto.response.CategoryResponse;
import per.khalilov.crudproject.models.Category;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
public class CategoryMapperTest {

    private static CategoryMapper categoryMapper;

    @BeforeAll
    public static void init() {
        categoryMapper = new CategoryMapper();
    }

    @Test
    public void toResponse() {
        UUID id = UUID.randomUUID();
        String url = "some_url";
        Category category = Category.builder()
                .id(id)
                .url(url)
                .build();
        assertThat(categoryMapper.toResponse(category)).isEqualTo(
                CategoryResponse.builder()
                        .id(id)
                        .url(url)
                        .build()
        );
    }

    @Test
    public void fromUpdateRequest() {
        CategoryUpdateRequest request = CategoryUpdateRequest.builder()
                .url("some")
                .build();
        assertThat(categoryMapper.fromUpdateRequest(request)).isEqualTo(Category.builder()
                .url("some")
                .build());
    }
}
