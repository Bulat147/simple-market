package per.khalilov.crudproject.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import per.khalilov.crudproject.api.ProductsApi;
import per.khalilov.crudproject.dto.request.ProductUpdateRequest;
import per.khalilov.crudproject.dto.request.ProductRequest;
import per.khalilov.crudproject.dto.response.ProductResponse;
import per.khalilov.crudproject.services.ProductService;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductsController implements ProductsApi {

    private final ProductService productService;
    @Override
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @Override
    public ProductResponse create(ProductRequest product, String categoryUrl) {
        return productService.create(product, categoryUrl);
    }

    @Override
    public void deleteByVendorCode(String vendorCode) {
        productService.deleteByVendorCode(vendorCode);
    }

    @Override
    public ProductResponse updateByVendorCode(String vendorCode, ProductUpdateRequest product) {
        return productService.updateByVendorCode(vendorCode, product);
    }
}
