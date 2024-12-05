package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.iotstar.entity.Category;

public interface CategoryService {

	void deleteByCategoryId(Integer categoryId);

	long count();

	Optional<Category> findByCategoryId(Integer categoryId);

	List<Category> findAll();

	Page<Category> findAll(org.springframework.data.domain.Pageable pageable);

	List<Category> findAll(Sort sort);

	<S extends Category> S save(S entity);

	Page<Category> findByCategoryNameContaining(String keyword, Pageable pageable);

	Optional<Category> findByCategoryName(String categoryName);
}
