package per.khalilov.crudproject.repositories;


import per.khalilov.crudproject.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Product update(Product newProduct);
    void deleteByVendorCode(String vendorCode);
    List<Product> findAll();
    Optional<Product> findByVendorCode(String vendorCode);
}
