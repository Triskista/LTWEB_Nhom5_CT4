package vn.iotstar.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public void deleteByCategoryId(Integer categoryId) {
		categoryRepository.deleteById(categoryId);
		
	}

	@Override
	public long count() {
		return categoryRepository.count();
	}

	@Override
	public Optional<Category> findByCategoryId(Integer categoryId) {
		return categoryRepository.findByCategoryId(categoryId);
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Page<Category> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	@Override
	public List<Category> findAll(Sort sort) {
		return categoryRepository.findAll(sort);
	}

	@Override
	public <S extends Category> S save(S entity) {
		return categoryRepository.save(entity);
	}

	@Override
	public Page<Category> findByCategoryNameContaining(String keyword, Pageable pageable) {
		return categoryRepository.findByCategoryNameContaining(keyword, pageable);
	}

	@Override
	public Optional<Category> findByCategoryName(String categoryName) {
		return categoryRepository.findByCategoryName(categoryName);
	}

	
}
