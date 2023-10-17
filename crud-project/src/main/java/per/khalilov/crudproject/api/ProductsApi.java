package per.khalilov.crudproject.api;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import per.khalilov.crudproject.dto.request.ProductUpdateRequest;
import per.khalilov.crudproject.dto.request.ProductRequest;
import per.khalilov.crudproject.dto.response.ProductResponse;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

import static per.khalilov.crudproject.constants.ProductConstants.*;

@RequestMapping("/product")
@Validated
public interface ProductsApi {

    @GetMapping
    List<ProductResponse> findAll();

    @PostMapping(value = {"", "/{categoryUrl}"})
    @ResponseStatus(HttpStatus.CREATED)
    ProductResponse create(@Valid @RequestBody ProductRequest product, @PathVariable(required = false) String categoryUrl);

    @DeleteMapping("/{vendorCode}")
    void deleteByVendorCode(@Pattern(regexp = "[A-Z0-9]+", message = INCORRECT_VENDOR_CODE_MESSAGE)
                            @Length(min = 2, message = SHORT_VENDOR_CODE_MESSAGE)
                            @PathVariable String vendorCode);

    @PutMapping("/{vendorCode}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ProductResponse updateByVendorCode(@Pattern(regexp = "[A-Z0-9]+", message = INCORRECT_VENDOR_CODE_MESSAGE)
                               @Length(min = 2, message = SHORT_VENDOR_CODE_MESSAGE)
                               @PathVariable String vendorCode, @Valid @RequestBody ProductUpdateRequest product);

}
