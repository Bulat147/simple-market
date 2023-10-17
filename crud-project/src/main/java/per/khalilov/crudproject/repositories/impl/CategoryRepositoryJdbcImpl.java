package per.khalilov.crudproject.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import per.khalilov.crudproject.models.Category;
import per.khalilov.crudproject.repositories.CategoryRepository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryJdbcImpl implements CategoryRepository {

    private static final String SQL_SELECT_ALL = "select * from category c";
    private static final String SQL_SELECT_BY_URL = "select * from category c " +
            "where c.url = :url";
    private static final String SQL_SELECT_BY_ID = "select * from category c " +
            "where c.id = :id";
    private static final String SQL_DELETE_BY_ID = "delete from category c where c.id = :id";
    public static final String SQL_UPDATE_BY_ID = "update category set url = :url where id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Category> rowMapper = ((rs, rowNum) ->
            Category.builder()
                    .id(rs.getObject("id", UUID.class))
                    .url(rs.getString("url"))
                    .build()
    );

    @Override
    public Category save(Category category) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate());
        Map<String, Object> values = new HashMap<>();
        values.put("id", category.getId());
        values.put("url", category.getUrl());
        insert.withTableName("category")
                .execute(values);
        return category;
    }

    @Override
    public Category update(Category category) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", category.getId());
        values.put("url", category.getUrl());
        jdbcTemplate.update(SQL_UPDATE_BY_ID, values);
        return category;
    }

    @Override
    public void deleteById(UUID categoryId) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Collections.singletonMap("id", categoryId));
    }

    @Override
    public List<Category> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    @Override
    public Optional<Category> findById(UUID categoryId) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", categoryId);
        return jdbcTemplate.query(SQL_SELECT_BY_ID, values, rowMapper).stream().findFirst();
    }

    @Override
    public Optional<Category> findByUrl(String categoryUrl) {
        Map<String, Object> values = new HashMap<>();
        values.put("url", categoryUrl);
        return jdbcTemplate.query(SQL_SELECT_BY_URL, values, rowMapper).stream().findFirst();
    }
}
