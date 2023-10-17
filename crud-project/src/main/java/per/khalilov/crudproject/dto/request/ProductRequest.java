package per.khalilov.crudproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import static per.khalilov.crudproject.constants.ProductConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank
    @Length(min = 2, message = SHORT_VENDOR_CODE_MESSAGE)
    @Pattern(regexp = "[A-Z0-9]+")
    private String vendorCode;
    @NotBlank(message = EMPTY_NAME_MESSAGE)
    @Length(min = 2, message = SHORT_NAME_MESSAGE)
    private String name;
    @Positive(message = INCORRECT_PRICE_MESSAGE)
    private Float price;
    @PositiveOrZero(message = INCORRECT_COUNT_MESSAGE)
    private Integer count;
}
