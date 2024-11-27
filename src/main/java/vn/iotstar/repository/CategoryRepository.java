package vn.iotstar.repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Optional<Category> findByName(String categoryName);
	List<Category> findByCateGoryNameContaining(String keyword, Pageable pageable);
	List<Category> findAllByCategoryName(String categoryName);
}
