package per.khalilov.crudproject.mappers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import per.khalilov.crudproject.dto.request.ProductRequest;
import per.khalilov.crudproject.dto.request.ProductUpdateRequest;
import per.khalilov.crudproject.dto.response.ProductResponse;
import per.khalilov.crudproject.models.Product;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperTest {

    public static ProductMapper productMapper;

    @BeforeAll
    public static void init() {
        productMapper = new ProductMapper();
    }

    @Test
    public void fromUpdateDtoWithVendorCode() {
        String name = "name";
        Integer count = 10;
        Float price = 45f;
        String vcode = "VCODE";
        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .name(name)
                .count(count)
                .price(price)
                .build();
        Product product = productMapper.fromUpdateDtoWithVendorCode(vcode, request);
        assertThat(product).isEqualTo(Product.builder()
                .vendorCode(vcode)
                .name(name)
                .count(count)
                .price(price)
                .build()
        );
    }

    @Test
    public void fromStringArray() {
        String vendorCode = "VENDORC";
        String name = "name";
        String count = "14";
        String price = "15.5";
        String categoryId = UUID.randomUUID().toString();
        String[] strings = new String[]{vendorCode, name, price, count, categoryId};
        Product productSame = Product.builder()
                .vendorCode(vendorCode)
                .name(name)
                .price(Float.valueOf(price))
                .count(Integer.valueOf(count))
                .categoryId(UUID.fromString(categoryId))
                .build();
        Product product = productMapper.fromStringArray(strings);
        assertThat(product).isEqualTo(productSame);
    }

    @Test
    public void toStringArray() {
        String vendorCode = "VENDORC";
        String name = "name";
        String count = "14";
        String price = "15.5";
        String categoryId = UUID.randomUUID().toString();
        String[] strings = new String[]{vendorCode, name, price, count, categoryId};
        Product productSame = Product.builder()
                .vendorCode(vendorCode)
                .name(name)
                .price(Float.valueOf(price))
                .count(Integer.valueOf(count))
                .categoryId(UUID.fromString(categoryId))
                .build();
        assertThat(productMapper.toStringArray(productSame)).isEqualTo(strings);
    }

    @Test
    public void fromProductRequest() {
        String name = "some name";
        Integer count = 15;
        Float price = 14.5f;
        String vendorCode = "CODE";
        ProductRequest request = ProductRequest.builder()
                .name(name)
                .count(count)
                .price(price)
                .vendorCode(vendorCode)
                .build();
        Product product = productMapper.fromProductRequest(request);
        Product product1 = Product.builder()
                .vendorCode(vendorCode)
                .name(name)
                .price(price)
                .count(count)
                .build();
        assertThat(product).isEqualTo(product1);
    }

    @Test
    public void toProductResponse() {
        String vendorCode = "VCODE";
        Integer count = 10;
        Float price = 14.5f;
        String name = "Name";
        UUID categoryId = UUID.randomUUID();
        Product product = Product.builder()
                .vendorCode(vendorCode)
                .count(count)
                .price(price)
                .name(name)
                .categoryId(categoryId)
                .build();
        ProductResponse response = productMapper.toProductResponse(product);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getPrice()).isEqualTo(price);
        assertThat(response.getCount()).isEqualTo(count);
        assertThat(response.getVendorCode()).isEqualTo(vendorCode);
        assertThat(response.getCategoryId()).isEqualTo(categoryId);
    }
}
