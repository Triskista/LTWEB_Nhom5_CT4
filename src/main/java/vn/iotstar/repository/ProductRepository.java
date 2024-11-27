package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByProductName(String productName);
	List<Product> findByProductNameContainingIgnoreCase(String productName);
	List<Product> findByPriceBetween(Float minPrice, Float maxPrice);
	List<Product> findByCategory_CategoryId(Integer categoryId);
	List<Product> findByStockGreaterThan(Integer stock);
	List<Product> findByPriceLessThan(Float price);
	List<Product> findByDescriptionContainingIgnoreCase(String description);
	List<Product> findAllByOrderByPriceAsc();
	List<Product> findAllByOrderByPriceDesc();
	List<Product> findAllByOrderByProductNameAsc();
	List<Product> findAllByOrderByProductNameDesc();

}
