package per.khalilov.crudproject.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import per.khalilov.crudproject.models.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.location=classpath:application-simple.yaml"})
public class ProductRepositoryFileImplTest {

    @Autowired
    ProductRepository productRepository;
    @Value("${project.holderFileDirectory}")
    private String holderFileDir;
    @Value("${project.holderFileName}")
    private String holderFileName;

    private static List<Product> products;

    @BeforeAll
    public static void init() {
        Product product = Product.builder()
                .categoryId(UUID.randomUUID())
                .name("name")
                .price(20f)
                .count(20)
                .vendorCode("VENDORC")
                .build();
        Product product1 = Product.builder()
                .categoryId(UUID.randomUUID())
                .name("namdfdfe")
                .price(10f)
                .count(203)
                .vendorCode("VENDORCODE")
                .build();
        products = List.of(product, product1);
    }

    @AfterEach
    public void setDown() {
        Path parents = Paths.get(holderFileDir);
        Path file = Paths.get(holderFileDir + "/" + holderFileName);
        try {
            Files.deleteIfExists(file);
            Files.deleteIfExists(parents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void when_save_successful_then_return_same_product() {
        assertThat(productRepository.save(products.get(0))).isEqualTo(products.get(0));
    }

    @Test
    public void when_save_then_truly_save_product() {
        Product product = products.get(0);
        productRepository.save(product);
        assertThat(productRepository.findByVendorCode(product.getVendorCode()).orElse(null)).isEqualTo(product);
    }

    @Test
    public void when_update_then_truly_update_product_and_return_same() {
        Product product = Product.builder()
                .categoryId(UUID.randomUUID())
                .name("name")
                .price(20f)
                .count(20)
                .vendorCode("VENDORC")
                .build();
        productRepository.save(product);
        product.setCount(100);
        assertThat(productRepository.update(product)).isEqualTo(product);
        assertThat(productRepository.findByVendorCode(product.getVendorCode()).orElse(null)).isEqualTo(product);
    }

    @Test
    public void when_delete_then_findByVendorCode_return_empty() {
        Product product = products.get(0);
        productRepository.save(product);
        assertThat(productRepository.findByVendorCode(product.getVendorCode()).orElse(null)).isEqualTo(product);
        productRepository.deleteByVendorCode(product.getVendorCode());
        assertThat(productRepository.findByVendorCode(product.getVendorCode()).orElse(null)).isEqualTo(null);
    }

    @Test
    public void when_findAll_then_return_all_products() {
        productRepository.save(products.get(0));
        productRepository.save(products.get(1));
        assertThat(productRepository.findAll()).isEqualTo(products);
    }

}
