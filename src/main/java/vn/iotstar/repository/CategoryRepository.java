package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Optional<Category> findByCategoryName(String categoryName);
	Page<Category> findByCategoryNameContaining(String keyword, Pageable pageable);
	List<Category> findAllByCategoryName(String categoryName);
	Optional<Category> findByCategoryId(Integer categoryId);
}
