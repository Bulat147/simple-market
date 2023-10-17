package per.khalilov.crudproject.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import per.khalilov.crudproject.dto.response.ProductResponse;
import per.khalilov.crudproject.dto.request.ProductUpdateRequest;
import per.khalilov.crudproject.exceptions.EntityAlreadyExistsException;
import per.khalilov.crudproject.exceptions.NoSuchEntityException;
import per.khalilov.crudproject.mappers.ProductMapper;
import per.khalilov.crudproject.dto.request.ProductRequest;
import per.khalilov.crudproject.models.Product;
import per.khalilov.crudproject.repositories.CategoryRepository;
import per.khalilov.crudproject.repositories.ProductRepository;
import per.khalilov.crudproject.services.ProductService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static per.khalilov.crudproject.constants.CategoryConstants.DEFAULT_CATEGORY_URL;
import static per.khalilov.crudproject.constants.CategoryConstants.NO_SUCH_CATEGORY_EXCEPTION_MESSAGE;
import static per.khalilov.crudproject.constants.ProductConstants.PRODUCT_ALREADY_EXISTS_EXCEPTION_MESSAGE;
import static per.khalilov.crudproject.constants.ProductConstants.NO_SUCH_PRODUCT_EXCEPTION_MESSAGE;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public ProductResponse create(ProductRequest productRequest, String categoryUrl) {
        Product product = productMapper.fromProductRequest(productRequest);
        productRepository.findByVendorCode(product.getVendorCode()).ifPresent(product1 -> {
            throw new EntityAlreadyExistsException(PRODUCT_ALREADY_EXISTS_EXCEPTION_MESSAGE);
        });
        UUID categoryId = categoryRepository.findByUrl(Objects.requireNonNullElse(categoryUrl, DEFAULT_CATEGORY_URL))
                .orElseThrow(() -> new NoSuchEntityException(NO_SUCH_CATEGORY_EXCEPTION_MESSAGE))
                .getId();
        product.setCategoryId(categoryId);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public void deleteByVendorCode(String vendorCode) {
        productRepository.findByVendorCode(vendorCode).orElseThrow(() ->
                new NoSuchEntityException(NO_SUCH_PRODUCT_EXCEPTION_MESSAGE));
        productRepository.deleteByVendorCode(vendorCode);
    }

    @Override
    public ProductResponse updateByVendorCode(String vendorCode, ProductUpdateRequest productUpdateRequest) {
        Product newProduct = productMapper.fromUpdateDtoWithVendorCode(vendorCode, productUpdateRequest);
        UUID categoryId = productUpdateRequest.getCategoryId();
        if (categoryId != null) {
            categoryRepository.findById(categoryId).orElseThrow(() ->
                    new NoSuchEntityException(NO_SUCH_CATEGORY_EXCEPTION_MESSAGE));
        }
        Product product = productRepository.findByVendorCode(vendorCode).orElseThrow(() ->
                new NoSuchEntityException(NO_SUCH_PRODUCT_EXCEPTION_MESSAGE));
        product.merge(newProduct);
        return productMapper.toProductResponse(
                productRepository.update(product)
        );
    }
}
