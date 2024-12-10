package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByProductNameContainingIgnoreCase(String productName);
	List<Product> findByPriceBetween(Float minPrice, Float maxPrice);
	List<Product> findAllByOrderByPriceAsc();
	List<Product> findAllByOrderByProductNameAsc();
	Optional<Product> findByProductName(String productname);
	Page<Product> findByProductNameContaining(String name,Pageable
			pageable);
	Product findByProductId(Integer productId);
	List<Product> findByProductNameContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCase(String productName, String categoryName);
<<<<<<< HEAD
	Page<Product> findByProductNameContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCase(String productName, String categoryName, Pageable pageable);
=======
	List<Product> findByCategory_CategoryName(String categoryName);
	List<Product> findByCategory_CategoryId(Integer categoryId);

>>>>>>> 07b202e73e36251df281a67acdaf382eb5059b9c

}
