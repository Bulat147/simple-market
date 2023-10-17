package per.khalilov.crudproject.services;

import per.khalilov.crudproject.dto.request.ProductRequest;
import per.khalilov.crudproject.dto.request.ProductUpdateRequest;
import per.khalilov.crudproject.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();
    ProductResponse create(ProductRequest productRequest, String categoryUrl);
    void deleteByVendorCode(String vendorCode);
    ProductResponse updateByVendorCode(String vendorCode, ProductUpdateRequest productUpdateRequest);
}
