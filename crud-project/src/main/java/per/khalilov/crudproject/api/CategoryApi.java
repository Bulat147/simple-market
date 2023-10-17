package per.khalilov.crudproject.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import per.khalilov.crudproject.dto.request.CategoryRequest;
import per.khalilov.crudproject.dto.request.CategoryUpdateRequest;
import per.khalilov.crudproject.dto.response.CategoryResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/category")
@Validated
public interface CategoryApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryResponse create(@Valid @RequestBody CategoryRequest request);

    @GetMapping
    List<CategoryResponse> findAll();

    @GetMapping("/{categoryId}")
    CategoryResponse findById(@PathVariable UUID categoryId);

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    CategoryResponse updateById(@Valid @RequestBody CategoryUpdateRequest updateRequest, @PathVariable UUID categoryId);

    @DeleteMapping("/{categoryId}")
    void deleteById(@PathVariable UUID categoryId);

}
