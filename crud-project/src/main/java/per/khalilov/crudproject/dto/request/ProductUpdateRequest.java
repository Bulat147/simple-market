package per.khalilov.crudproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

import static per.khalilov.crudproject.constants.ProductConstants.INCORRECT_PRICE_MESSAGE;
import static per.khalilov.crudproject.constants.ProductConstants.SHORT_NAME_MESSAGE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateRequest {

    @Length(min = 2, message = SHORT_NAME_MESSAGE)
    private String name;
    @Positive(message = INCORRECT_PRICE_MESSAGE)
    private Float price;
    @PositiveOrZero(message = INCORRECT_PRICE_MESSAGE)
    private Integer count;
    private UUID categoryId;

}
