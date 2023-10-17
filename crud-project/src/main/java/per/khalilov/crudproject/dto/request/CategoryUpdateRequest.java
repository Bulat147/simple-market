package per.khalilov.crudproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static per.khalilov.crudproject.constants.CategoryConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryUpdateRequest {

    @NotBlank(message = EMPTY_URL_MESSAGE)
    @Pattern(regexp = "[a-zA-Z0-9_]+")
    @Length(max = 60, message = LONG_URL_MESSAGE)
    private String url;

}
