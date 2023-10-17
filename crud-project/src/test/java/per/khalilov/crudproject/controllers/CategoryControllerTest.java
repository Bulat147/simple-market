package per.khalilov.crudproject.controllers;

import org.junit.jupiter.api.Test;
import per.khalilov.crudproject.dto.request.CategoryRequest;
import per.khalilov.crudproject.dto.request.CategoryUpdateRequest;
import per.khalilov.crudproject.dto.response.CategoryResponse;
import per.khalilov.crudproject.dto.response.ExceptionDto;
import per.khalilov.crudproject.exceptions.EntityAlreadyExistsException;
import per.khalilov.crudproject.exceptions.NoSuchEntityException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static per.khalilov.crudproject.constants.CategoryConstants.DEFAULT_CATEGORY_URL;

public class CategoryControllerTest extends AbstractControllerIntegrationTest {

    @Test
    public void when_create_with_short_or_long_name_then_return_bad_request() throws Exception {
        CategoryRequest request = new CategoryRequest("s");
        mockMvc.perform(post("/category")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void when_findById_with_not_existing_id_then_return_null() throws Exception {
        mockMvc.perform(get("/category/" + UUID.randomUUID()))
                .andExpect(content().string(""));
    }

    @Test
    public void when_updateById_with_existing_url_then_return_bad_request_with_exception_dto() throws Exception {
        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("some name")
                .build();
        CategoryResponse response = objectMapper.readValue(mockMvc.perform(post("/category")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(categoryRequest))
                ).andReturn().getResponse().getContentAsString(), CategoryResponse.class);
        CategoryUpdateRequest updateRequest = CategoryUpdateRequest.builder()
                .url(DEFAULT_CATEGORY_URL)
                .build();
        ExceptionDto exceptionDto = objectMapper.readValue(mockMvc.perform(put("/category/" + response.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(), ExceptionDto.class);
        assertThat(exceptionDto.getExceptionName()).isEqualTo(EntityAlreadyExistsException.class.getSimpleName());
    }

    @Test
    public void when_updateById_with_not_existing_id_then_return_bad_request_with_exception_dto() throws Exception {
        ExceptionDto exceptionDto = objectMapper.readValue(mockMvc.perform(put("/category/" + UUID.randomUUID())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(CategoryUpdateRequest.builder()
                                .url("new").build())))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(), ExceptionDto.class);
        assertThat(exceptionDto.getExceptionName()).isEqualTo(NoSuchEntityException.class.getSimpleName());
    }

}
