package per.khalilov.crudproject.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private String vendorCode;
    private String name;
    private Float price;
    private Integer count;
    private UUID categoryId;

    public void merge(Product src) {
        name = src.getName() == null ? name : src.getName();
        price = src.getPrice() == null ? price : src.getPrice();
        count = src.getCount() == null ? count : src.getCount();
        categoryId = src.getCategoryId() == null ? categoryId : src.getCategoryId();
    }
}
