package per.khalilov.crudproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponse {
    private String vendorCode;
    private String name;
    private Float price;
    private Integer count;
    private UUID categoryId;
}
