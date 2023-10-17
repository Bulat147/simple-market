package per.khalilov.crudproject.repositories.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import per.khalilov.crudproject.models.Product;
import per.khalilov.crudproject.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@ConditionalOnProperty(value = "project.repository.fileImplementation.enabled", havingValue ="false")
public class ProductRepositoryInMemoryImpl implements ProductRepository {

    private final ConcurrentMap<String, Product> products = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        products.put(product.getVendorCode(), product);
        return product;
    }

    @Override
    public Product update(Product newProduct) {
        products.put(newProduct.getVendorCode(), newProduct);
        return newProduct;
    }

    @Override
    public void deleteByVendorCode(String vendorCode) {
        products.remove(vendorCode);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Optional<Product> findByVendorCode(String vendorCode) {
        return Optional.ofNullable(products.get(vendorCode));
    }
}
