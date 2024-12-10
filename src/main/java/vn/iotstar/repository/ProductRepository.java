package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByProductNameContainingIgnoreCase(String productName);
	List<Product> findByPriceBetween(Float minPrice, Float maxPrice);
	List<Product> findAllByOrderByPriceAsc();
	List<Product> findAllByOrderByProductNameAsc();
	Optional<Product> findByProductName(String productname);
	 Page<Product> findByProductNameContaining(String name, Pageable pageable);

	    @Query("SELECT p FROM Product p WHERE p.category.categoryName = :categoryName")
	    Page<Product> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
	Product findByProductId(Integer productId);
	List<Product> findByProductNameContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCase(String productName, String categoryName);
	List<Product> findByCategory_CategoryName(String categoryName);
	List<Product> findByCategory_CategoryId(Integer categoryId);


}
