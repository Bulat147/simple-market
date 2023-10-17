package per.khalilov.crudproject.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import per.khalilov.crudproject.dto.request.CategoryRequest;
import per.khalilov.crudproject.dto.request.CategoryUpdateRequest;
import per.khalilov.crudproject.dto.response.CategoryResponse;
import per.khalilov.crudproject.dto.response.ProductResponse;
import per.khalilov.crudproject.exceptions.EntityAlreadyExistsException;
import per.khalilov.crudproject.exceptions.NoSuchEntityException;
import per.khalilov.crudproject.mappers.CategoryMapper;
import per.khalilov.crudproject.mappers.ProductMapper;
import per.khalilov.crudproject.models.Category;
import per.khalilov.crudproject.models.Product;
import per.khalilov.crudproject.models.utils.UrlProvider;
import per.khalilov.crudproject.repositories.CategoryRepository;
import per.khalilov.crudproject.repositories.ProductRepository;
import per.khalilov.crudproject.services.impl.CategoryServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static per.khalilov.crudproject.constants.CategoryConstants.DEFAULT_CATEGORY_URL;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    private ProductMapper productMapper;
    private CategoryService categoryService;

    private static List<Category> categories;
    private static List<Product> products;

    @BeforeAll
    public static void init() {
        UUID firstCategoryId = UUID.randomUUID();
        UUID secondCategoryId = UUID.randomUUID();
        Category firstCategory = Category.builder()
                .id(firstCategoryId)
                .url(UrlProvider.getUrlFromName("детские товары"))
                .build();
        Category secondCategory = Category.builder()
                .id(secondCategoryId)
                .url(UrlProvider.getUrlFromName("иные"))
                .build();
        Product productOne = Product.builder()
                .categoryId(firstCategoryId)
                .price(34f)
                .count(34)
                .vendorCode("SOME456")
                .name("Product1")
                .build();
        Product productTwo = Product.builder()
                .categoryId(firstCategoryId)
                .price(35f)
                .count(35)
                .vendorCode("SOME45")
                .name("Product2")
                .build();
        Product productThree = Product.builder()
                .categoryId(secondCategoryId)
                .price(36f)
                .count(36)
                .vendorCode("SOME4")
                .name("Product3")
                .build();
        categories = List.of(firstCategory, secondCategory);
        products = List.of(productOne, productTwo, productThree);
    }

    @BeforeEach
    public void setUp() {
        productMapper = new ProductMapper();
        categoryService = new CategoryServiceImpl(categoryRepository, productRepository,
                new CategoryMapper(), productMapper);
    }

    @Test
    public void when_create_category_by_name_with_existing_url_then_throw_exception() {
        when(categoryRepository.findByUrl(UrlProvider.getUrlFromName("детские товары")))
                .thenReturn(Optional.of(categories.get(0)));
        CategoryRequest categoryRequest = new CategoryRequest("детские товары");
        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> categoryService.create(categoryRequest));
    }

    @Test
    public void when_create_then_return_not_empty_response_dto() {
        when(categoryRepository.findByUrl(notNull())).thenReturn(Optional.empty());
        when(categoryRepository.save(notNull())).thenReturn(categories.get(0));
        CategoryRequest categoryRequest = new CategoryRequest("детские товары");
        CategoryResponse response = categoryService.create(categoryRequest);
        assertThat(response.getUrl()).isNotBlank();
        assertThat(response.getId()).isNotNull();
    }

    @Test
    public void when_findAll_then_return_valid_response_dto_with_categories_products() {
        ProductResponse productResponseOne = productMapper.toProductResponse(products.get(0));
        ProductResponse productResponseTwo = productMapper.toProductResponse(products.get(1));
        ProductResponse productResponseThree = productMapper.toProductResponse(products.get(2));

        when(categoryRepository.findAll()).thenReturn(categories);
        when(productRepository.findAll()).thenReturn(products);
        List<CategoryResponse> responses = categoryService.findAll();

        assertThat(responses.get(0)).isNotNull();
        assertThat(responses.get(1)).isNotNull();
        assertThat(responses.get(0).getId()).isNotEqualTo(responses.get(1).getId());

        assertThat(responses.get(1).getProducts()).isEqualTo(List.of(productResponseThree));
        assertThat(responses.get(0).getProducts()).isEqualTo(List.of(productResponseOne, productResponseTwo));
    }

    @Test
    public void when_findById_with_not_existing_id_then_return_null() {
        when(categoryRepository.findById(notNull())).thenReturn(Optional.empty());
        assertThat(categoryService.findById(UUID.randomUUID())).isNull();
    }

    @Test
    public void when_findById_then_return_not_empty_response_dto_with_product_list() {
        List<ProductResponse> productResponses = List.of(productMapper.toProductResponse(products.get(0)),
                productMapper.toProductResponse(products.get(1)));

        when(productRepository.findAll()).thenReturn(products);
        when(categoryRepository.findById(categories.get(0).getId())).thenReturn(Optional.of(categories.get(0)));

        CategoryResponse categoryResponse = categoryService.findById(categories.get(0).getId());
        assertThat(categoryResponse.getUrl()).isNotBlank();
        assertThat(categoryResponse.getId()).isEqualTo(categories.get(0).getId());
        assertThat(categoryResponse.getProducts()).isEqualTo(productResponses);
    }

    @Test
    public void when_deleteById_then_default_category_not_deleted() {
        UUID categoryId = UUID.randomUUID();
        Category category = Category.builder()
                .id(categoryId)
                .url(DEFAULT_CATEGORY_URL)
                .build();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        categoryService.deleteById(categoryId);
        verify(categoryRepository, times(0)).deleteById(categoryId);
    }

    @Test
    public void when_deleteById_then_delete_if_existing() {
        when(categoryRepository.findById(categories.get(0).getId())).thenReturn(Optional.of(categories.get(0)));
        categoryService.deleteById(categories.get(0).getId());
        verify(categoryRepository, times(1)).deleteById(categories.get(0).getId());
        categoryService.deleteById(UUID.randomUUID());
        // проверим что вызов так и остался один, их не стало два
        verify(categoryRepository, times(1)).deleteById(categories.get(0).getId());
    }

    @Test
    public void when_updateById_with_not_existing_category_then_throw_exception() {
         when(categoryRepository.findById(any())).thenReturn(Optional.empty());
         assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
             categoryService.updateById(new CategoryUpdateRequest("some"), UUID.randomUUID())
         );
    }

    @Test
    public void when_updateById_with_existing_url_then_throw_exception() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(categories.get(0)));
        when(categoryRepository.findByUrl(categories.get(0).getUrl())).thenReturn(Optional.of(categories.get(1)));
        assertThatExceptionOfType(EntityAlreadyExistsException.class).isThrownBy(() ->
                categoryService.updateById(new CategoryUpdateRequest(categories.get(0).getUrl()), UUID.randomUUID())
        );
    }

    @Test
    public void when_updateById_with_not_existing_url_and_existing_category_then_update() {
        String newUrl = "new_url";
        when(categoryRepository.findById(categories.get(0).getId())).thenReturn(Optional.of(categories.get(0)));
        when(categoryRepository.findByUrl(newUrl)).thenReturn(Optional.empty());
        when(categoryRepository.update(any())).thenReturn(Category.builder()
                .id(categories.get(0).getId())
                .url(newUrl)
                .build());
        categoryService.updateById(new CategoryUpdateRequest(newUrl), categories.get(0).getId());
        verify(categoryRepository, times(1)).update(any());
    }

    @Test
    public void when_updateById_with_existing_url_and_same_id_then_dont_update_but_return_response_dto() {
        String newUrl = categories.get(0).getUrl();
        when(categoryRepository.findById(categories.get(0).getId())).thenReturn(Optional.of(categories.get(0)));
        when(categoryRepository.findByUrl(newUrl)).thenReturn(Optional.of(categories.get(0)));
        CategoryResponse response = categoryService.updateById(new CategoryUpdateRequest(newUrl),
                categories.get(0).getId());
        verify(categoryRepository, times(0)).update(any());
        assertThat(response).isNotNull();
    }

    @Test
    public void when_updateById_successful_then_return_valid_response_dto() {
        UUID id = UUID.randomUUID();
        String newUrl = "new_url";
        Category category = Category.builder()
                .id(id)
                .url("old_url")
                .build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.findByUrl(newUrl)).thenReturn(Optional.empty());
        category.setUrl(newUrl);
        when(categoryRepository.update(category)).thenReturn(category);
        CategoryResponse categoryResponse = categoryService.updateById(new CategoryUpdateRequest(newUrl), id);
        assertThat(categoryResponse.getId()).isEqualTo(id);
        assertThat(categoryResponse.getUrl()).isEqualTo(newUrl);
    }

}
