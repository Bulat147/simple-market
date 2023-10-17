package per.khalilov.crudproject.mappers;

import org.springframework.stereotype.Component;
import per.khalilov.crudproject.dto.request.ProductUpdateRequest;
import per.khalilov.crudproject.dto.request.ProductRequest;
import per.khalilov.crudproject.dto.response.ProductResponse;
import per.khalilov.crudproject.models.Product;

import java.util.UUID;

@Component
public class ProductMapper {

    public Product fromUpdateDtoWithVendorCode(String vendorCode, ProductUpdateRequest productUpdateRequest) {
        return Product.builder()
                .vendorCode(vendorCode)
                .name(productUpdateRequest.getName())
                .count(productUpdateRequest.getCount())
                .price(productUpdateRequest.getPrice())
                .build();
    }

    public Product fromStringArray(String[] values) {
        // Думаю, здесь не нужна обработка исключений - метод понадобится только при доставании
        // из csv файла, а там все предворительно записано верно,
        // так как валидация этих значений проходит ещё на уровне контроллеров (точнее до обработки контроллерами)
        return Product.builder()
                .vendorCode(values[0])
                .name(values[1])
                .price(Float.valueOf(values[2]))
                .count(Integer.valueOf(values[3]))
                .categoryId(UUID.fromString(values[4]))
                .build();
    }

    public String[] toStringArray(Product product) {
        return new String[]{product.getVendorCode(),
                product.getName(),
                product.getPrice().toString(),
                product.getCount().toString(),
                product.getCategoryId().toString()};
    }
    
    public Product fromProductRequest(ProductRequest productRequest) {
        return Product.builder()
                .vendorCode(productRequest.getVendorCode())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .count(productRequest.getCount())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .vendorCode(product.getVendorCode())
                .name(product.getName())
                .categoryId(product.getCategoryId())
                .price(product.getPrice())
                .count(product.getCount())
                .build();
    }
}
