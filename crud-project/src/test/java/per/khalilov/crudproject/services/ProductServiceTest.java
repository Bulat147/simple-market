package per.khalilov.crudproject.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import per.khalilov.crudproject.dto.request.ProductRequest;
import per.khalilov.crudproject.dto.request.ProductUpdateRequest;
import per.khalilov.crudproject.dto.response.ProductResponse;
import per.khalilov.crudproject.exceptions.EntityAlreadyExistsException;
import per.khalilov.crudproject.exceptions.NoSuchEntityException;
import per.khalilov.crudproject.mappers.ProductMapper;
import per.khalilov.crudproject.models.Category;
import per.khalilov.crudproject.models.Product;
import per.khalilov.crudproject.repositories.CategoryRepository;
import per.khalilov.crudproject.repositories.ProductRepository;
import per.khalilov.crudproject.services.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static per.khalilov.crudproject.constants.CategoryConstants.DEFAULT_CATEGORY_URL;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    private ProductMapper productMapper;
    private ProductService productService;
    private static final List<Product> products = new ArrayList<>();

    // Возможно стоит убрать тесты где проверяется валидность dto и оставить просто проверку вызова репозитория,
    // валидность dto можно будет проверить при интеграционных тестах

    @BeforeAll
    public static void init() {
        Product product1 = Product.builder()
                .vendorCode("SDFAS5T")
                .name("ASFASD")
                .count(34)
                .price(343.54F)
                .categoryId(UUID.randomUUID())
                .build();
        Product product2 = Product.builder()
                .vendorCode("SDFASsdfsa")
                .name("ASFAafasd")
                .count(454)
                .price(34.54F)
                .categoryId(UUID.randomUUID())
                .build();
        Product product3 = Product.builder()
                .vendorCode("SDasc5T")
                .name("ASFasdsadD")
                .count(64)
                .price(3.54F)
                .categoryId(UUID.randomUUID())
                .build();
        products.add(product1);
        products.add(product2);
        products.add(product3);
    }

    @BeforeEach
    public void setUp() {
        productMapper = new ProductMapper();
        productService = new ProductServiceImpl(productRepository, categoryRepository, productMapper);
    }

    @Test
    public void when_findAll_then_return_all_products() {
        when(productRepository.findAll()).thenReturn(products);
        List<ProductResponse> expectResponses = products.stream().map(productMapper::toProductResponse).toList();
        assertThat(productService.findAll())
                .isEqualTo(expectResponses);
    }

    @Test
    public void when_create_with_existing_vendorCode_then_throw_exception() {
        String vendorCode = products.get(0).getVendorCode();
        when(productRepository.findByVendorCode(vendorCode))
                .thenReturn(Optional.of(products.get(0)));
        ProductRequest request = ProductRequest.builder()
                .vendorCode(vendorCode)
                .price(36f)
                .count(10)
                .name("name")
                .build();
        assertThatExceptionOfType(EntityAlreadyExistsException.class).isThrownBy(() ->
            productService.create(request, null)
        );
    }

    @Test
    public void when_create_with_null_categoryUrl_then_save_with_default_categoryId() {
        when(productRepository.findByVendorCode(any())).thenReturn(Optional.empty());
        UUID id = UUID.randomUUID();
        Category category = Category.builder()
                .id(id)
                .url(DEFAULT_CATEGORY_URL)
                .build();
        when(categoryRepository.findByUrl(DEFAULT_CATEGORY_URL)).thenReturn(Optional.of(category));
        ProductRequest request = ProductRequest.builder()
                .vendorCode("VCODE")
                .count(10)
                .name("some product")
                .price(45.5f)
                .build();
        Product product = productMapper.fromProductRequest(request);
        product.setCategoryId(id);
        when(productRepository.save(product))
                .thenReturn(product);
        ProductResponse response = productService.create(request, null);
        assertThat(response.getCategoryId())
                .isEqualTo(id);
    }

    @Test
    public void when_create_with_not_existing_categoryUrl_then_throw_exception() {
        ProductRequest request = ProductRequest.builder()
                .vendorCode("VCODE")
                .count(10)
                .name("some product")
                .price(45.5f)
                .build();
        when(productRepository.findByVendorCode(any()))
                .thenReturn(Optional.empty());
        when(categoryRepository.findByUrl(any()))
                .thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityException.class)
                .isThrownBy(() ->
            productService.create(request, "any_url")
        );
    }

    @Test
    public void when_create_with_valid_not_null_categoryUrl_then_return_valid_response_dto() {
        String url = "some_url";
        UUID id = UUID.randomUUID();
        Category category = Category.builder()
                .id(id)
                .url(url)
                .build();
        when(categoryRepository.findByUrl(url)).thenReturn(Optional.of(category));
        when(productRepository.findByVendorCode(any())).thenReturn(Optional.empty());
        ProductRequest request = ProductRequest.builder()
                .vendorCode("SOME")
                .price(24f)
                .count(20)
                .name("product")
                .build();
        Product product = productMapper.fromProductRequest(request);
        product.setCategoryId(id);
        when(productRepository.save(product)).thenReturn(product);
        ProductResponse response = productMapper.toProductResponse(product);
        assertThat(productService.create(request, url)).isEqualTo(response);
    }

    @Test
    public void when_delete_with_not_existing_vendorCode_then_throw_exception() {
        when(productRepository.findByVendorCode(any())).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
            productService.deleteByVendorCode("some")
        );
    }

    @Test
    public void when_delete_with_existing_vendorCode_then_delete() {
        when(productRepository.findByVendorCode(any())).thenReturn(Optional.of(products.get(0)));
        productService.deleteByVendorCode(products.get(0).getVendorCode());
        verify(productRepository, times(1)).deleteByVendorCode(products.get(0).getVendorCode());
    }

    @Test
    public void when_update_with_not_existing_categoryId_then_throw_exception() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .categoryId(id)
                .build();
        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
           productService.updateByVendorCode("SOME", request)
        );
    }

    @Test
    public void when_update_with_not_existing_product_then_throw_exception() {
        when(productRepository.findByVendorCode(any())).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
            productService.updateByVendorCode("SOME", ProductUpdateRequest.builder()
                    .price(34f)
                    .build())
        );
    }

    @Test
    public void when_update_with_valid_parameters_then_return_not_empty_response_dto() {
        Product product = Product.builder()
                .count(10)
                .categoryId(UUID.randomUUID())
                .name("name")
                .price(34f)
                .count(10)
                .build();
        when(productRepository.findByVendorCode(any())).thenReturn(Optional.of(product));
        product.setCount(14);
        when(productRepository.update(product)).thenReturn(product);
        ProductResponse productResponse = productService.updateByVendorCode("SOME",
                ProductUpdateRequest.builder()
                        .count(14)
                        .build());
        assertThat(productResponse).isEqualTo(productMapper.toProductResponse(product));
    }

}
