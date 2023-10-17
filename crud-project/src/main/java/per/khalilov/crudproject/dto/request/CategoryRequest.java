package per.khalilov.crudproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static per.khalilov.crudproject.constants.CategoryConstants.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryRequest {
    @NotBlank(message = EMPTY_CYRILLIC_NAME_MESSAGE)
    @Length(min = 2, message = SHORT_CYRILLIC_NAME_MESSAGE)
    @Length(max = 60, message = LONG_CYRILLIC_NAME_MESSAGE)
    @Pattern(regexp = "[а-яА-Яa-zA-Z0-9 ]+")
    private String name;

}
